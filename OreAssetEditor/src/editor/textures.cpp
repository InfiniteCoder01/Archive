#include "common.hpp"
#include "editor.hpp"

namespace Textures {
#pragma region Atlas
static bool showAtlasSettingsPopup = false;
bool showAtlas = false;
std::vector<std::unique_ptr<Atlas>> atlases;
Atlas* currentAtlas;
vec2i selectedTile = 0;

Atlas* atlasByName(const std::string& name) {
  for (auto& atlas : atlases) {
    if (atlas->name == name) return atlas.get();
  }
  return atlases.empty() ? nullptr : atlases[0].get();
}

uint16_t atlasIndexByName(const std::string& name) {
  for (uint32_t i = 0; i < atlases.size(); i++) {
    if (atlases[i]->name == name) return i;
  }
  return 0;
}

bool chooseAtlas(const std::string& label, Atlas*& atlas, int8_t tilesetness) {
  if (!atlas) atlas = atlases[0].get();
  if (!label.empty()) {
    ImGui::TextUnformatted(label.c_str());
    ImGui::SameLine();
  }
  bool modified = false;
  if (ImGui::BeginCombo("##AtlasInput", atlas->name.c_str())) {
    for (auto& item : atlases) {
      if (tilesetness != -1 && !item->tileset == tilesetness) continue;
      if (ImGui::Selectable(item->name.c_str(), item.get() == atlas)) atlas = item.get(), modified = true;
      if (item.get() == atlas) ImGui::SetItemDefaultFocus();
    }
    ImGui::EndCombo();
  }
  return modified;
}

void importAtlas(const std::string& filename) {
  if (filename.empty()) return;
  currentAtlas = new Atlas(filename, 16);
  atlases.push_back(std::unique_ptr<Atlas>(currentAtlas));
  atlasSettings();
}

void atlasSettings() {
  showAtlasSettingsPopup = true;
  selectedTile = 0;
}

static void atlasWindow() {
  if (!ImGui::Begin("Texture Atlas", &showAtlas)) return ImGui::End();

  if (currentAtlas) {
    chooseAtlas("", currentAtlas);

    vec2i viewportPos = oreVec(ImGui::GetCursorScreenPos());
    float scale = min(ImGui::GetContentRegionAvail().x / currentAtlas->image.width, ImGui::GetContentRegionAvail().y / currentAtlas->image.height);
    ImGui::Image(imID(currentAtlas->image), imVec(currentAtlas->image.size() * scale));
    if (currentAtlas->tileset) {
      for (auto& patch : currentAtlas->tileset->patches) {
        vec2i start = viewportPos + patch * currentAtlas->tilesize * scale;
        ImGui::GetWindowDrawList()->AddRect(imVec(start), imVec(start + currentAtlas->tilesize * vec2i(4, 1) * scale), MvColor::red.value, 0.f, 0, 2.f);
      }
      if (currentAtlas->tileset->colliders) {
        for (int i = 0; i < currentAtlas->width() * currentAtlas->height(); i++) {
          if (currentAtlas->tileset->colliders[i]) {
            ImGui::GetWindowDrawList()->AddCircleFilled(imVec(viewportPos + (vec2f(i % currentAtlas->width(), i / currentAtlas->width()) + 0.5f) * currentAtlas->tilesize * scale), 5, MvColor::red.value);
          }
        }
      }
    }

    vec2i tileOnMouse = (vec2f)(oreVec(ImGui::GetMousePos()) - viewportPos) / currentAtlas->tilesize / scale;
    if (ImGui::IsItemHovered() && (Mova::isMouseButtonHeld(MOUSE_LEFT) || Mova::isMouseButtonHeld(MOUSE_RIGHT))) {
      if (currentAtlas->tileset && currentAtlas->tileset->colliders) {
        if (Mova::isKeyHeld(MvKey::Ctrl)) {
          currentAtlas->tileset->colliders[currentAtlas->toIndex(tileOnMouse)] = Mova::isMouseButtonHeld(MOUSE_LEFT);
        }
      }
      currentComponent = nullptr;
    }

    if ((ImGui::IsItemClicked() || ImGui::IsItemHovered()) && Mova::isMouseButtonPressed(MOUSE_LEFT) && !Mova::isKeyHeld(MvKey::Ctrl)) {
      selectedTile = tileOnMouse;
      if (currentAtlas->tileset) {
        if (currentAtlas->tileset->inPatch(selectedTile)) selectedTile = currentAtlas->tileset->patch(selectedTile);
      }
      currentComponent = nullptr;
    }
    if (!Mova::isKeyHeld(MvKey::Ctrl) && ImGui::BeginPopupContextWindow()) {
      if (!currentAtlas->tileset) {
        if (ImGui::MenuItem("Enable tileset")) currentAtlas->tileset = new Atlas::Tileset();
      } else {
        if (ImGui::MenuItem("Disable tileset")) delete currentAtlas->tileset, currentAtlas->tileset = nullptr;
      }
      if (currentAtlas->tileset) {
        if (!currentAtlas->tileset->inPatch(selectedTile)) {
          if (ImGui::MenuItem("Add patch")) currentAtlas->tileset->patches.push_back(selectedTile);
        } else {
          if (ImGui::MenuItem("Remove patch")) currentAtlas->tileset->removePatch(selectedTile);
        }
        if (!currentAtlas->tileset->colliders) {
          if (ImGui::MenuItem("Enable colliders")) currentAtlas->tileset->colliders = new bool[currentAtlas->width() * currentAtlas->height()], memset(currentAtlas->tileset->colliders, 0, currentAtlas->width() * currentAtlas->height());
        } else {
          if (ImGui::MenuItem("Disable colliders")) delete[] currentAtlas->tileset->colliders, currentAtlas->tileset->colliders = nullptr;
        }
      }
      ImGui::EndPopup();
    }
    {
      vec2i start = viewportPos + selectedTile * currentAtlas->tilesize * scale;
      ImGui::GetWindowDrawList()->AddRect(imVec(start), imVec(start + currentAtlas->tilesize * scale), MvColor::red.value, 0.f, 0, 3.f);
    }
  } else ImGui::TextUnformatted("\"File->Import texture atlas\" to import texture atlas!");
  ImGui::End();
}

#pragma endregion Atlas
#pragma region Component
std::vector<std::unique_ptr<Component>> components;
Component* currentComponent;
bool showComponents = false;

void Component::remove() {
  for (auto& child : children) {
    child->parentObject->components.erase(std::remove_if(child->parentObject->components.begin(), child->parentObject->components.end(), [&child](const TiledLevel::ComponentInstance& instance) { return &instance == child; }), child->parentObject->components.end());
  }
  fs::remove(path);
}

static void addPackageComponents() {
  Component* atlasRenderer = new Component("Atlas Renderer (Builtin)", "package/builtin/atlasRenderer");
  atlasRenderer->properties.push_back(Component::Property("Atlas", PropertyType::ATLAS));
  components.push_back(std::unique_ptr<Component>(atlasRenderer));
}

Component* getComponentByPath(fs::path path) {
  for (auto& component : components) {
    if (component->path == path) return component.get();
  }
  return nullptr;
}

void componentPropertyInput(std::string& value, PropertyType type) {
  const char* ID = ("##PropertyInput" + std::to_string((uintptr_t)&value)).c_str();
  if (type == PropertyType::STRING) {
    char buffer[256];
    strncpy(buffer, value.c_str(), 255);
    if (ImGui::InputTextEx(ID, "Property Value", buffer, sizeof(buffer), imVec(vec2i(0)), 0)) value = buffer;
  } else if (type == PropertyType::INT) {
    int buffer;
    try {
      buffer = std::stoi(value);
    } catch (...) {
      buffer = 0, value = "0";
    }
    if (ImGui::InputScalar(ID, ImGuiDataType_S32, &buffer)) value = std::to_string(buffer);
  } else if (type == PropertyType::FLOAT) {
    float buffer;
    try {
      buffer = std::stof(value);
    } catch (...) {
      buffer = 0, value = "0";
    }
    if (ImGui::InputFloat(ID, &buffer)) value = std::to_string(buffer);
  } else if (type == PropertyType::ATLAS) {
    Atlas* buffer = atlasByName(value);
    if (chooseAtlas("", buffer)) value = buffer->name;
  } else if (type == PropertyType::LEVEL) {
    TiledLevel::Level* buffer = TiledLevel::levelByName(value);
    if (TiledLevel::chooseLevel("", buffer)) value = buffer->name;
  }
}

static void componentsWindow() {
  static char tmpFolder[256];
  static bool creatingFolder = false;

  if (!ImGui::Begin("Components", &showComponents)) return ImGui::End();
  if (projectSaveDirectory.empty()) {
    ImGui::TextUnformatted("Open Project to see the components");
    return ImGui::End();
  }

  static fs::path path;
  fs::path base = projectSaveDirectory + "components";
  if (path.empty()) path = base;  // FIXME: Potentional issue with opening another project and closing this
  if (path != base) {
    if (ImGui::Button("..")) path = path.parent_path();
  }
  for (const auto& p : fs::directory_iterator(path)) {
    if (!p.is_directory()) continue;
    if (ImGui::Button(p.path().stem().string().c_str())) path = p.path();
  }
  for (uint32_t i = 0; i < components.size(); i++) {
    if (components[i]->path.parent_path() != path) continue;
    if (ImGui::Button(components[i]->name.c_str())) currentComponent = components[i].get();
    ImGui::SameLine();
    uint32_t deleteW = ImGui::CalcTextSize(ICON_FA_TRASH).x + ImGui::GetStyle().FramePadding.x;
    ImGui::SetCursorPosX(ImGui::GetContentRegionMax().x - deleteW);                                                                           // TODO: remove folders, rename
    if (ImGui::Selectable(ICON_FA_TRASH, false, 0, ImVec2(deleteW, 0))) components[i]->remove(), components.erase(components.begin() + i--);  // TODO: maybe not that easy to delete everything
  }

  if (ImGui::BeginPopupModal("Create Component", nullptr, ImGuiWindowFlags_AlwaysAutoResize)) {  // TODO: Component settings
    static char name[256];
    UI::formField("Component name: ", name, sizeof(name));
    if (ImGui::Button("Ok")) {
      currentComponent = new Component(name, (path / (std::string(name) + ".obj")).string());
      currentComponent->save();
      components.push_back(std::unique_ptr<Component>(currentComponent));
      std::sort(components.begin(), components.end(), [](const std::unique_ptr<Component>& a, const std::unique_ptr<Component>& b) { return strcmp(a->name, b->name); });
      ImGui::CloseCurrentPopup();
    }
    ImGui::SameLine();
    if (ImGui::Button("Cancel")) ImGui::CloseCurrentPopup();
    ImGui::EndPopup();
  }

  if (creatingFolder) {
    if (ImGui::InputText("##Filename Input", tmpFolder, sizeof(tmpFolder), ImGuiInputTextFlags_EnterReturnsTrue)) {
      fs::create_directory(path / tmpFolder);
      creatingFolder = false;
    }
  }
  if (ImGui::Button("+")) {
    ImGui::OpenPopup("Create Component");
  }
  if (ImGui::Button("+ Folder")) creatingFolder = true;

  ImGui::End();
}

#pragma endregion Component
#pragma region Inspector
bool showInspector = false;
static void inspectorWindow() {
  if (!ImGui::Begin("Inspector", &showInspector)) return ImGui::End();
  if (currentComponent) {
    ImGui::Text("Component '%s'", currentComponent->name.c_str());
    ImGui::Separator();

    ImGui::TextUnformatted("Component Properties:");
    for (int i = 0; i < currentComponent->properties.size(); i++) {
      ImGui::PushID(i);
      // Name
      char buffer[256];
      strcpy(buffer, currentComponent->properties[i].name.c_str());
      float width = (ImGui::GetContentRegionAvail().x - ImGui::CalcTextSize("-").x - 10 - ImGui::GetStyle().FramePadding.x * 10) / 5;
      ImGui::SetNextItemWidth(width * 2);
      if (ImGui::InputTextEx("##PropertyNameInput", "Property name", buffer, sizeof(buffer), imVec(vec2i(0)), 0)) {
        currentComponent->properties[i].name = buffer;
      }

      // Type
      ImGui::SameLine();
      ImGui::SetNextItemWidth(width * 2);
      if (ImGui::BeginCombo("##PropertyType", propertyTypes[(int)currentComponent->properties[i].type].c_str())) {
        for (int j = 0; j < IM_ARRAYSIZE(propertyTypes); j++) {
          if (ImGui::Selectable(propertyTypes[j].c_str(), j == (int)currentComponent->properties[i].type)) currentComponent->properties[i].type = (PropertyType)j;
          if (j == (int)currentComponent->properties[i].type) ImGui::SetItemDefaultFocus();
        }
        ImGui::EndCombo();
      }

      // Value & Extras
      ImGui::SameLine();
      ImGui::SetNextItemWidth(width);
      componentPropertyInput(currentComponent->properties[i].defaultValue, currentComponent->properties[i].type);
      ImGui::SameLine();
      if (ImGui::Button("X")) {
        currentComponent->properties.erase(currentComponent->properties.begin() + i);
        for (auto child : currentComponent->children) child->properties.erase(child->properties.begin() + i);
        i--;
        ImGui::PopID();
        continue;
      }
      ImGui::PopID();
      ImGui::SameLine();
      ImGui::Selectable(("=##Reorder" + currentComponent->properties[i].name).c_str(), false, 0, ImVec2(0, ImGui::GetFrameHeight()));
      ImGui::SetCursorPosY(ImGui::GetCursorPosY() - ImGui::GetStyle().ItemSpacing.y);
      if (ImGui::IsItemActive() && !ImGui::IsItemHovered()) {
        int next = i + (ImGui::GetMouseDragDelta(0).y < 0.f ? -1 : 1);
        if (next >= 0 && next < currentComponent->properties.size()) {
          std::iter_swap(currentComponent->properties.begin() + i, currentComponent->properties.begin() + next);
          for (auto child : currentComponent->children) std::iter_swap(child->properties.begin() + i, child->properties.begin() + next);
          ImGui::ResetMouseDragDelta();
        }
      }
    }
    if (ImGui::Button("+")) {
      currentComponent->properties.emplace_back();
      for (auto child : currentComponent->children) child->properties.emplace_back();
    }
  } else if (TiledLevel::currentObject) {
    ImGui::TextUnformatted("Object");
    ImGui::TextUnformatted("Components:");
    for (int i = 0; i < TiledLevel::currentObject->components.size(); i++) {
      ImGui::PushID(i);
      ImGui::Separator();
      ImGui::TextUnformatted(TiledLevel::currentObject->components[i].parent->name.c_str());
      ImGui::SameLine();

      int selectW = ImGui::CalcTextSize("Select").x, propsW = ImGui::CalcTextSize("...").x, framePadding = ImGui::GetStyle().FramePadding.x;
      if (!TiledLevel::currentObject->components[i].parent->isFromPackage()) {
        ImGui::SetCursorPosX(ImGui::GetContentRegionMax().x - selectW - framePadding - propsW - framePadding);
        if (ImGui::Selectable("Select", false, 0, ImVec2(selectW, 0))) break;
        ImGui::SameLine();
      }
      ImGui::SetCursorPosX(ImGui::GetContentRegionMax().x - propsW - framePadding);
      if (ImGui::Selectable("...", false, 0, ImVec2(propsW, 0))) ImGui::OpenPopup("ComponentProperties");

      if (ImGui::BeginPopup("ComponentProperties")) {  // TODO: Component commands
        if (i > 0 && ImGui::MenuItem("Up")) {
          std::iter_swap(TiledLevel::currentObject->components.begin() + i, TiledLevel::currentObject->components.begin() + i - 1);
        }
        if (i < TiledLevel::currentObject->components.size() - 1 && ImGui::MenuItem("Down")) {
          std::iter_swap(TiledLevel::currentObject->components.begin() + i, TiledLevel::currentObject->components.begin() + i + 1);
        }
        if (ImGui::MenuItem("Remove")) {
          TiledLevel::currentObject->components.erase(TiledLevel::currentObject->components.begin() + i--);
          ImGui::EndPopup();
          ImGui::PopID();
          continue;
        }
        ImGui::EndPopup();
      }
      for (uint32_t j = 0; j < TiledLevel::currentObject->components[i].properties.size(); j++) {
        Component::Property& property = TiledLevel::currentObject->components[i].parent->properties[j];
        std::string& value = TiledLevel::currentObject->components[i].properties[j];
        ImGui::TextUnformatted(property.name.c_str());
        ImGui::SameLine();
        ImGui::SetNextItemWidth(-1);
        componentPropertyInput(value, property.type);
      }

      //     char buffer[256];
      //     strcpy(buffer, TiledLevel::object->properties[i].c_str());
      //     ImGui::SetNextItemWidth(ImGui::GetContentRegionAvail().x - ImGui::GetStyle().FramePadding.x * 2);
      //     if (ImGui::InputTextEx(("##PropertyValueInput" + std::to_string(i)).c_str(), "Property Value", buffer, sizeof(buffer), imVec(vec2i(0)), 0)) {
      //       TiledLevel::object->properties[i] = buffer;
      //     }
      ImGui::PopID();
    }
    ImGui::Separator();
    {
      static int currentItem;
      static bool addComponentPopup = false;
      static std::vector<std::string> componentLabels;
      if (!addComponentPopup && ImGui::Button("Add Component")) {
        addComponentPopup = true;
        componentLabels.clear();
        componentLabels.reserve(components.size());
        for (auto& component : components) componentLabels.push_back(component->name);
        currentItem = -1;
      } else if (addComponentPopup) {
        if (ImGui_ComboFilter("##component", componentLabels, currentItem)) {
          if (currentItem != -1) TiledLevel::currentObject->components.push_back(TiledLevel::ComponentInstance(components[currentItem].get(), TiledLevel::currentObject));
          addComponentPopup = false;
        }
      }
    }
    if (ImGui::Button("Delete Object")) {
      const auto pred = [](const TiledLevel::Object& object) { return &object == TiledLevel::currentObject; };
      TiledLevel::currentLevel->objects.erase(std::remove_if(TiledLevel::currentLevel->objects.begin(), TiledLevel::currentLevel->objects.end(), pred), TiledLevel::currentLevel->objects.end());
      TiledLevel::currentObject = nullptr;
    }
  }
  ImGui::End();
}
#pragma endregion Inspector

void save() {
  for (auto& atlas : atlases) atlas->save();
  for (auto& component : components) component->save();
}

void load() {
  {  // Atlases
    atlases.clear();
    for (const auto& entry : fs::directory_iterator(projectSaveDirectory + "atlases/")) {
      if (entry.path().extension() == ".png") atlases.push_back(std::unique_ptr<Atlas>(new Atlas(entry.path().stem().string())));
    }
    std::sort(atlases.begin(), atlases.end(), [](const std::unique_ptr<Atlas>& a, const std::unique_ptr<Atlas>& b) { return strcmp(a->name, b->name); });
    currentAtlas = atlases.empty() ? nullptr : atlases[0].get();
  }

  {  // Components
    components.clear();
    addPackageComponents();
    for (const auto& p : fs::recursive_directory_iterator(projectSaveDirectory + "components/")) {
      if (p.is_directory()) continue;
      components.push_back(std::unique_ptr<Component>(new Component(p.path().string())));
    }
  }
}

void exportData() {
  std::string data = "const uint8_t PROGMEM textures[] = {\n  ";

  data += itobytes(atlases.size(), 2);
  for (auto& atlas : atlases) {
    int nTiles = atlas->width() * atlas->height();
    data += format("%d, %d, %d, ", nTiles, atlas->tilesize.x, atlas->tilesize.y);
    for (int i = 0; i < nTiles; i++) {
      for (int y = 0; y < atlas->tilesize.y; y++) {
        for (int x = 0; x < atlas->tilesize.x; x++) {
          uint16_t pixel = rgb565(atlas->image.getPixel(i * atlas->tilesize.x % atlas->image.width + x, i * atlas->tilesize.x / atlas->image.width * atlas->tilesize.y + y));
          data += format("%d, %d, ", pixel >> 8, pixel & 0xff);
        }
      }
    }
    data += format("%d, ", atlas->tileset != nullptr);
    if (atlas->tileset) {
      data += format("%d, ", atlas->tileset->patches.size());
      for (const auto& patch : atlas->tileset->patches) {
        data += format("%d, ", patch.x + patch.y * atlas->width() + 1);
      }
      data += format("%d, ", atlas->tileset->colliders != nullptr);
      if (atlas->tileset->colliders) {
        int tilesTotal = atlas->width() * atlas->height();
        for (int i = 0; i < tilesTotal / 8 + (tilesTotal % 8 != 0); i++) {
          uint8_t byte = 0;
          for (int j = 0; j < 8; j++) {
            if (i * 8 + j < tilesTotal) byte |= atlas->tileset->colliders[i * 8 + j] << j;
          }
          data += format("%d, ", byte);
        }
      }
    }
  }
  for (int i = 0; i < 2; i++) data.pop_back();
  data += "\n};";
  Mova::copyToClipboard(data);
}

void windows() {
  if (showAtlasSettingsPopup) ImGui::OpenPopup("Atlas settings"), showAtlasSettingsPopup = false;
  if (ImGui::BeginPopupModal("Atlas settings", nullptr, ImGuiWindowFlags_AlwaysAutoResize)) {
    static char buffer[256] = {'\1'};
    if (buffer[0] == '\1') strncpy(buffer, currentAtlas->name.c_str(), sizeof(buffer) - 1);
    currentAtlas->tilesize = max(currentAtlas->tilesize, vec2(1));
    UI::formField("Atlas name: ", buffer, sizeof(buffer));
    UI::formField("Tile size: ", currentAtlas->tilesize);
    vec2i tileCount = currentAtlas->image.size() / currentAtlas->tilesize;
    if (UI::formField("Tile count: ", tileCount)) currentAtlas->tilesize = currentAtlas->image.size() / tileCount;
    if (ImGui::Button("Ok")) {
      currentAtlas->name = buffer;
      buffer[0] = '\1';
      std::sort(atlases.begin(), atlases.end(), [](const std::unique_ptr<Atlas>& a, const std::unique_ptr<Atlas>& b) { return strcmp(a->name, b->name); });
      ImGui::CloseCurrentPopup();
    }
    ImGui::EndPopup();
  }

  if (showAtlas) atlasWindow();
  if (showInspector) inspectorWindow();
  if (showComponents) componentsWindow();
}
}  // namespace Textures
