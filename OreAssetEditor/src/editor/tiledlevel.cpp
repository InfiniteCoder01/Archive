#include "common.hpp"
#include "editor.hpp"

namespace TiledLevel {
Object* currentObject;

vec2i objectSize(Object& object) {
  for (const auto& component : object.components) {
    if (component.parent->path == "package/builtin/atlasRenderer") return Textures::atlasByName(component.properties[0])->tilesize;
  }
  return currentLevel->tileset->tilesize;
}

#pragma region Level
static bool showLevelSettingsPopup = false;
bool showEditor = false;
std::vector<std::unique_ptr<Level>> levels;
Level* currentLevel;

Level* levelByName(const std::string& name) {
  for (auto& level : levels) {
    if (level->name == name) return level.get();
  }
  return levels.empty() ? nullptr : levels[0].get();
}

uint16_t levelIndexByName(const std::string& name) {
  for (uint16_t i = 0; i < levels.size(); i++) {
    if (levels[i]->name == name) return i;
  }
  return 0;
}

bool chooseLevel(const std::string& label, Level*& level) {
  if (!level) level = levels[0].get();
  if (!label.empty()) {
    ImGui::TextUnformatted(label.c_str());
    ImGui::SameLine();
  }
  bool modified = false;
  if (ImGui::BeginCombo("##LevelInput", level->name.c_str())) {
    for (auto& item : levels) {
      if (ImGui::Selectable(item->name.c_str(), item.get() == level)) level = item.get(), modified = true;
      if (item.get() == level) ImGui::SetItemDefaultFocus();
    }
    ImGui::EndCombo();
  }
  return modified;
}

void newLevel() { currentLevel = nullptr, showLevelSettingsPopup = true; }
void levelSettings() { showLevelSettingsPopup = true; }

static bool concatX(vec2i tile, vec2i pos, int dir) { return inRange(pos.x + dir, 0, (int)currentLevel->width) && currentLevel->getTile(pos + vec2i(dir, 0)) == currentLevel->tileset->tileset->patch(tile); }
static bool concatY(vec2i tile, vec2i pos, int dir) { return inRange(pos.y + dir, 0, (int)currentLevel->height) && currentLevel->getTile(pos + vec2i(0, dir)) == currentLevel->tileset->tileset->patch(tile); }

static void drawQuater(MvDrawTarget& viewport, vec2i pos, vec2i tile, vec2f screen, vec2f tileScreenSize, vec2i quater) {
  vec2i delta = quater * 2 - 1;
  if (!concatX(tile, pos, delta.x)) tile.x += concatY(tile, pos, delta.y) ? 3 : 1;
  else if (!concatY(tile, pos, delta.y)) tile.x += 2;
  viewport.drawImage(currentLevel->tileset->image, floor(screen + quater * tileScreenSize / 2), ceil(tileScreenSize / 2), (tile * 2 + quater) * currentLevel->tileset->tilesize / 2, currentLevel->tileset->tilesize / 2);
}

static void drawPatch(MvDrawTarget& viewport, vec2i pos, vec2i tile, vec2i screen, vec2i tileScreenSize) {
  drawQuater(viewport, pos, tile, screen, tileScreenSize, vec2i(0, 0));
  drawQuater(viewport, pos, tile, screen, tileScreenSize, vec2i(1, 0));
  drawQuater(viewport, pos, tile, screen, tileScreenSize, vec2i(0, 1));
  drawQuater(viewport, pos, tile, screen, tileScreenSize, vec2i(1, 1));
}

void editor() {
  static std::unique_ptr<MvImage> viewport = nullptr;
  static vec2f camera = 0;
  static float scale = 3;
  if (!ImGui::Begin("Tiled Level Editor", &showEditor)) return ImGui::End();
  if (!levels.empty()) {
    if (!currentLevel) currentLevel = levels[0].get();
    if (ImGui::BeginCombo("##LevelSelect", currentLevel->name.c_str())) {  // TODO: extract
      for (auto& item : levels) {
        if (ImGui::Selectable(item->name.c_str(), item.get() == currentLevel)) currentLevel = item.get();
        if (item.get() == currentLevel) ImGui::SetItemDefaultFocus();
      }
      ImGui::EndCombo();
    }
  }
  vec2i viewportSize = availableRegion();
  vec2i viewportPos = oreVec(ImGui::GetCursorScreenPos());
  vec2f mouse = oreVec(ImGui::GetMousePos()) - viewportPos;
  if (!viewport || viewport->width != viewportSize.x || viewport->height != viewportSize.y) viewport = std::unique_ptr<MvImage>(new MvImage(max(viewportSize, vec2i(1)), nullptr));

  if (currentLevel && currentLevel->tileset) {
    viewport->clear(MvColor::black);
    vec2f tileScreenSize = currentLevel->tileset->tilesize * scale;
    viewport->fillRect(-camera, tileScreenSize * currentLevel->size(), MvColor(135, 206, 235));
    for (int x = max(camera.x / tileScreenSize.x, 0); x <= min((camera.x + viewport->width) / tileScreenSize.x, currentLevel->width - 1); x++) {
      for (int y = max(camera.y / tileScreenSize.y, 0); y <= min((camera.y + viewport->height) / tileScreenSize.y, currentLevel->height - 1); y++) {
        vec2i tile = currentLevel->getTile(vec2i(x, y));
        vec2f screen = vec2f(x, y) * tileScreenSize - camera;
        if (tile != -1) {
          if (currentLevel->tileset->tileset->inPatch(tile)) drawPatch(*viewport, vec2i(x, y), currentLevel->tileset->tileset->patch(tile), screen, tileScreenSize);
          else viewport->drawImage(currentLevel->tileset->image, screen, tileScreenSize, tile * currentLevel->tileset->tilesize, currentLevel->tileset->tilesize);
        }
      }
    }
    for (const auto& object : currentLevel->objects) {
      vec2i pos = (vec2f)object.pos / currentLevel->tileset->tilesize * tileScreenSize - camera;
      bool found = false;
      for (const auto& component : object.components) {
        if (component.parent->path == "package/builtin/atlasRenderer") {
          found = true;
          auto atlas = Textures::atlasByName(component.properties[0]);
          viewport->drawImage(atlas->image, pos, atlas->tilesize * scale, 0, atlas->tilesize);
        }
      }
      if (!found) viewport->drawRect(pos, currentLevel->tileset->tilesize * scale, MvColor::red);
    }
    ImGui::Image(imID(*viewport), imVec(viewportSize));

    if (ImGui::IsItemHovered()) {
      vec2i tileOnMouse = (mouse + camera) / tileScreenSize;
      if (tileOnMouse.x >= 0 && tileOnMouse.x < currentLevel->width && tileOnMouse.y >= 0 && tileOnMouse.y < currentLevel->height) {
        if (Mova::isMouseButtonHeld(MOUSE_LEFT) && !Mova::isKeyHeld(MvKey::Ctrl)) {
          bool found = false;
          for (auto& object : currentLevel->objects) {
            if (inRangeW<vec2i>(mouse, (vec2f)object.pos / currentLevel->tileset->tilesize * tileScreenSize - camera, objectSize(object) * scale)) {
              currentObject = &object;
              Textures::currentComponent = nullptr;
              found = true;
              break;
            }
          }
          if (!found) {
            if (Textures::currentAtlas == currentLevel->tileset) currentLevel->setTile(tileOnMouse, Textures::selectedTile);
            else currentLevel->objects.push_back(Object(Mova::isKeyHeld(MvKey::Alt) ? (mouse + camera) / scale : (vec2f)tileOnMouse * currentLevel->tileset->tilesize, Textures::currentAtlas));
          }
        } else if (Mova::isMouseButtonHeld(MOUSE_RIGHT) && !Mova::isKeyHeld(MvKey::Ctrl)) {
          bool found = false;
          for (int i = 0; i < currentLevel->objects.size(); i++) {
            if (inRangeW<vec2i>((mouse + camera) / scale, currentLevel->objects[i].pos, objectSize(currentLevel->objects[i]))) {
              if (&currentLevel->objects[i] == currentObject) currentObject = nullptr;
              currentLevel->objects.erase(currentLevel->objects.begin() + i);
              found = true;
              break;
            }
          }
          if (!found) currentLevel->setTile(tileOnMouse, -1);
        }
        vec2i selectedScreen = viewportPos + tileOnMouse * tileScreenSize - camera;
        ImGui::GetWindowDrawList()->AddRect(imVec(selectedScreen), imVec(selectedScreen + tileScreenSize), MvColor::red.value);
      }
      status = format("Tile at %d, %d", tileOnMouse.x, tileOnMouse.y);

      if (ImGui::IsWindowHovered()) {
        float lastScale = scale;
        scale = max(scale + Mova::getScrollY() * 0.4f, min(viewportSize.x / float(currentLevel->width * currentLevel->tileset->tilesize.x), viewportSize.y / float(currentLevel->height * currentLevel->tileset->tilesize.y)));
        if (Mova::getScrollY()) camera = (mouse + camera) * scale / lastScale - mouse;
        if (Mova::isMouseButtonHeld(MOUSE_LEFT) && Mova::isKeyHeld(MvKey::Ctrl) || Mova::isMouseButtonHeld(MOUSE_MIDDLE)) camera -= Mova::getMouseDelta();
      }
    }
    camera = max(vec2i(0), min(currentLevel->size() * currentLevel->tileset->tilesize * scale - viewportSize, camera));
  } else ImGui::TextUnformatted("\"File->New level\" to create new level!");

  ImGui::End();
}
#pragma endregion Level

void save() {
  for (auto& level : levels) level->save();
}

void load() {
  for (const auto& entry : fs::directory_iterator(projectSaveDirectory + "levels/")) {
    if (entry.path().extension() == ".lvl") levels.push_back(std::unique_ptr<Level>(new Level(entry.path().stem().string())));
  }
  currentLevel = levels.empty() ? nullptr : levels[0].get();
}

void exportData() {
  std::string data = "const uint8_t PROGMEM levels[] = {\n  ";
  data += itobytes(levels.size(), 2);
  for (const auto& level : levels) {
    data += itobytes(level->width, 2);
    data += itobytes(level->height, 2);
    for (int y = 0; y < level->height; y++) {
      for (int x = 0; x < level->width; x++) {
        vec2i tile = level->getTile(vec2i(x, y));
        if (tile == -1) data += "0, ";
        else data += itobytes(tile.x + tile.y * level->tileset->image.width / level->tileset->tilesize.x + 1, 1);
      }
    }
    uint32_t objectsDataSizeInsert = data.size();

    data += itobytes(level->objects.size(), 2);
    for (const auto& object : level->objects) {
      data += itobytes(object.pos.x, 2) + itobytes(object.pos.y, 2) + itobytes(object.components.size(), 2);
      for (const auto& component : object.components) {
        data += itobytes(std::find_if(Textures::components.begin(), Textures::components.end(), [&component](const std::unique_ptr<Textures::Component>& cmp) { return component.parent->path == cmp->path; }) - Textures::components.begin(), 2);

        for (int i = 0; i < component.properties.size(); i++) {
          auto type = component.parent->properties[i].type;
          auto value = component.properties[i];
          if (type == Textures::PropertyType::INT) data += itobytes(std::stoi(value), 4);
          else if (type == Textures::PropertyType::STRING) {
            for (auto character : value) data += format("%d, ", (uint8_t)character);
            data += "0, ";
          } else if (type == Textures::PropertyType::ATLAS) data += itobytes(Textures::atlasIndexByName(value), 2);
          else if (type == Textures::PropertyType::LEVEL) data += itobytes(levelIndexByName(value), 2);
        }
      }
    }

    data.insert(objectsDataSizeInsert, itobytes(std::count(data.begin() + objectsDataSizeInsert, data.end(), ','), 4));
  }
  for (int i = 0; i < 2; i++) data.pop_back();
  data += "\n};";
  Mova::copyToClipboard(data);
}

void windows() {
  if (showLevelSettingsPopup) ImGui::OpenPopup("Level settings");
  // if (ImGui::BeginPopupModal("Level settings", nullptr, ImGuiWindowFlags_AlwaysAutoResize)) {
  //   static vec2i levelSize;
  //   static char levelName[256];
  //   static Textures::Atlas* tileset;
  //   if (level && showLevelSettingsPopup) {
  //     levelSize = level->size();
  //     strncpy(levelName, level->name.c_str(), sizeof(levelName));
  //     tileset = level->tileset;
  //   }

  //   levelSize = max(levelSize, vec2i(1));
  //   UI::formField("Level size: ", levelSize);
  //   if (!level) UI::formField("Level name: ", levelName, sizeof(levelName));
  //   Textures::chooseAtlas("Level tileset: ", tileset, 1);

  //   bool disabled = false;
  //   if (!level) {
  //     disabled = levelName[0] == '\0';
  //     if (!disabled) {
  //       for (const auto level : levels) {
  //         if (level->name == levelName) {
  //           disabled = true;
  //           break;
  //         }
  //       }
  //     }
  //   }

  //   ImGui::BeginDisabled(disabled);
  //   if (ImGui::Button("Ok")) {
  //     if (!level) levels.push_back(level = new Level(levelName, tileset, levelSize.x, levelSize.y));
  //     else {
  //       level->resize(levelSize);
  //       level->tileset = tileset;
  //     }
  //     ImGui::CloseCurrentPopup();
  //   }
  //   ImGui::EndDisabled();
  //   ImGui::EndPopup();
  // }
  // if (showLevelSettingsPopup) showLevelSettingsPopup = false;

  if (showEditor) editor();
}
}  // namespace TiledLevel
