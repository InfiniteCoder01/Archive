#include "common.hpp"
#include <shellapi.h>

extern std::string status, projectSaveDirectory;

namespace TiledLevel {
struct ComponentInstance;
}

namespace Textures {
#pragma region Atlas
struct Atlas {
  vec2i tilesize;
  std::string name;
  MvImage image;
  struct Tileset {
    std::vector<vec2i> patches;
    bool* colliders = nullptr;

    vec2i patch(vec2i tile) {
      for (const auto& patch : patches) {
        if (inRangeW(tile.x, patch.x, 4) && tile.y == patch.y) return patch;
      }
      return -1;
    }

    bool inPatch(vec2i tile) { return patch(tile) != -1; }

    void removePatch(vec2i tile) {
      for (uint16_t i = 0; i < patches.size(); i++) {
        if (inRangeW(tile.x, patches[i].x, 4) && tile.y == patches[i].y) {
          patches.erase(patches.begin() + i);
          break;
        }
      }
    }
  }* tileset = nullptr;

  Atlas(const std::string& filename, vec2i tilesize) : tilesize(tilesize), image(filename) {}
  Atlas(const std::string& name) : name(name), image(projectSaveDirectory + "atlases/" + name + ".png") {
    File file(projectSaveDirectory + "atlases/" + name + ".atl", "rb");
    fread((void*)&tilesize, sizeof(tilesize), 1, file());
    if (fgetn<bool>(file())) {
      tileset = new Tileset();
      uint16_t nPatches = fgetn<uint16_t>(file());
      tileset->patches.resize(nPatches);
      if (nPatches) fread((void*)&tileset->patches[0], sizeof(tileset->patches[0]), nPatches, file());
      if (fgetn<bool>(file())) {
        tileset->colliders = new bool[width() * height()];
        uint32_t w, h;
        readMetadata(file(), "%32i %32i", &w, &h);
        bool* readColliders = new bool[w * h];
        fread((void*)readColliders, sizeof(tileset->colliders[0]), w * h, file());
        for (uint16_t x = 0; x < min(w, width()); x++) {
          for (uint16_t y = 0; y < min(h, height()); y++) {
            tileset->colliders[x + y * width()] = readColliders[x + y * w];
          }
        }
        delete[] readColliders;
      }
    }
  }

  ~Atlas() {
    if (tileset) {
      if (tileset->colliders) delete[] tileset->colliders;
      delete tileset;
    }
  }

  void save() {
    image.save(projectSaveDirectory + "atlases/" + name + ".png");
    File file(projectSaveDirectory + "atlases/" + name + ".atl", "wb+");
    fwrite((void*)&tilesize, sizeof(tilesize), 1, file());
    fputn<bool>(file(), tileset != nullptr);
    if (tileset) {
      fputn<uint16_t>(file(), tileset->patches.size());
      if (!tileset->patches.empty()) fwrite((void*)&tileset->patches[0], sizeof(tileset->patches[0]), tileset->patches.size(), file());
      fputn<bool>(file(), tileset->colliders != nullptr);
      if (tileset->colliders) {
        writeMetadata(file(), "%32i %32i", width(), height());
        fwrite((void*)tileset->colliders, sizeof(tileset->colliders[0]), width() * height(), file());
      }
    }
  }

  uint16_t width() const { return image.width / tilesize.x; }
  uint16_t height() const { return image.height / tilesize.y; }
  vec2i size() const { return vec2i(width(), height()); }
  uint32_t toIndex(vec2i tile) const { return tile.x + tile.y * width(); }
};

extern bool showAtlas;
extern std::vector<std::unique_ptr<Atlas>> atlases;
extern Atlas* currentAtlas;
extern vec2i selectedTile;

Atlas* atlasByName(const std::string& name);
uint16_t atlasIndexByName(const std::string& name);
bool chooseAtlas(const std::string& label, Atlas*& atlas, int8_t tilesetness = -1);
void importAtlas(const std::string& filename);
void atlasSettings();

#pragma endregion Atlas
#pragma region Component
enum class PropertyType : uint8_t { INT, FLOAT, STRING, ATLAS, LEVEL };
const std::string propertyTypes[] = {"int", "float", "String", "Atlas", "Level"};

struct Component {
  std::string name;
  fs::path path;

  struct Property {
    std::string name, defaultValue;
    PropertyType type = PropertyType::INT;

    Property() = default;
    Property(const std::string& name, PropertyType type, const std::string& defaultValue = "") : name(name), defaultValue(defaultValue), type(type) {}
  };

  std::vector<Property> properties;
  std::vector<TiledLevel::ComponentInstance*> children;

  Component(const std::string& name, const fs::path& path) : name(name), path(path) {}
  Component(const std::string& path) : name(path.substr(path.find_last_of("/\\") + 1, path.size() - path.find_last_of("/\\") - 5)), path(path) {
    File file(path, "rb");
    uint32_t nProps = fgetn<uint32_t>(file());
    properties.resize(nProps);
    for (auto& property : properties) {
      readMetadata(file(), "%s, %s, %8i", &property.name, &property.defaultValue, &property.type);
    }
  }

  bool isFromPackage() { return path.string().substr(0, 8) == "package/"; }

  void save() {
    if (isFromPackage()) return;
    File file(path.string(), "wb+");
    fputn<uint32_t>(file(), properties.size());
    for (const auto& property : properties) {
      writeMetadata(file(), "%s, %s, %8i", property.name.c_str(), property.defaultValue.c_str(), property.type);
    }
  }

  void remove();
};

extern bool showComponents;
extern std::vector<std::unique_ptr<Component>> components;
extern Component* currentComponent;

void componentPropertyInput(std::string& value, PropertyType type);
Component* getComponentByPath(fs::path path);

#pragma endregion Component

extern bool showInspector;
void save();
void load();
void exportData();
void windows();
}  // namespace Textures

namespace TiledLevel {
#pragma region ComponentsAndObjects
struct Object;
struct ComponentInstance {
  Object* parentObject;
  Textures::Component* parent;
  std::vector<std::string> properties;

  ComponentInstance() = default;
  ComponentInstance(Textures::Component* parent, Object* parentObject) : parent(parent), parentObject(parentObject) {
    parent->children.push_back(this);
    properties.resize(parent->properties.size());
    for (uint32_t i = 0; i < parent->properties.size(); i++) properties[i] = parent->properties[i].defaultValue;
  }

  ~ComponentInstance() { parent->children.erase(std::remove(parent->children.begin(), parent->children.end(), this), parent->children.end()); }
};

struct Object {
  vec2i pos;
  std::vector<ComponentInstance> components;

  Object() = default;
  Object(vec2i pos, Textures::Atlas* atlas) : pos(pos) {
    components.push_back(ComponentInstance(Textures::getComponentByPath("package/builtin/atlasRenderer"), this));
    components.rbegin()->properties[0] = atlas->name;
  }
};

extern Object* currentObject;

#pragma endregion ComponentsAndObjects
#pragma region Level
struct Level {
  Textures::Atlas* tileset;
  uint32_t width, height;
  std::string name;
  vec2i* data;
  std::vector<Object> objects;

  Level(const std::string& name) : name(name) {
    File file(projectSaveDirectory + "levels/" + name + ".lvl", "rb");
    std::string tilesetName;
    readMetadata(file(), "%32i, %32i, %s", &width, &height, &tilesetName);
    tileset = Textures::atlasByName(tilesetName);
    data = new vec2i[width * height];
    fread(data, sizeof(vec2i) * width * height, 1, file());
    uint16_t nObjects = fgetn<uint16_t>(file());
    objects.resize(nObjects);
    for (auto& object : objects) {
      uint32_t nComponents;
      readMetadata(file(), "%32i %32i %32i", &object.pos.x, &object.pos.y, &nComponents);
      object.components.resize(nComponents);
      for (auto& component : object.components) {
        std::string parentName = freadstr(file());
        for (const auto& parent : Textures::components) {
          if (parent->name == parentName) component = ComponentInstance(parent.get(), &object);
        }
        for (auto& property : component.properties) property = freadstr(file());
      }
    }
  }

  Level(const std::string& name, Textures::Atlas* tileset, uint32_t width, uint32_t height) : tileset(tileset), width(width), height(height), name(name) {
    data = new vec2i[width * height];
    std::fill(data, data + width * height, -1);
  }

  ~Level() {
    if (data) delete[] data;
  }

  void resize(vec2i size) {
    if (this->size() == size) return;
    vec2i* data_ = new vec2i[size.x * size.y];
    std::fill(data_, data_ + size.x * size.y, -1);

    for (uint16_t x = 0; x < min(width, size.x); x++) {
      for (uint16_t y = 0; y < min(height, size.y); y++) {
        data_[x + (size.y - y - 1) * size.x] = getTile(vec2i(x, height - y - 1));
      }
    }

    delete[] data;
    data = data_;
    width = size.x, height = size.y;
  }

  vec2i getTile(vec2i pos) { return data[pos.x + pos.y * width]; }
  void setTile(vec2i pos, vec2i tile) { data[pos.x + pos.y * width] = tile; }
  vec2i size() { return vec2i(width, height); }

  void save() {
    File file(projectSaveDirectory + "levels/" + name + ".lvl", "wb+");
    writeMetadata(file(), "%32i %32i %s %b %16i", width, height, tileset->name.c_str(), data, sizeof(vec2i) * width * height, objects.size());
    for (const auto& object : objects) {
      writeMetadata(file(), "%32i %32i %32i", object.pos.x, object.pos.y, object.components.size());
      for (const auto& component : object.components) {
        fwritestr(file(), component.parent->name);
        for (const auto& property : component.properties) fwritestr(file(), property);
      }
    }
  }
};

Level* levelByName(const std::string& name);
uint16_t levelIndexByName(const std::string& name);
bool chooseLevel(const std::string& label, Level*& level);

extern std::vector<std::unique_ptr<Level>> levels;
extern Level* currentLevel;
extern bool showEditor;

#pragma endregion Level

void newLevel();
void levelSettings();
void save();
void load();
void exportData();
void windows();
}  // namespace TiledLevel

static void loadProject(const std::string& folder) {
  projectSaveDirectory = folder;
  Textures::load();
  TiledLevel::load();
}

static void saveProject(const std::string& folder = "") {
  if (!folder.empty()) projectSaveDirectory = folder;
  Textures::save();
  TiledLevel::save();
}

static void openProjectsFolder() { ShellExecute(NULL, NULL, projectSaveDirectory.c_str(), NULL, NULL, SW_SHOWNORMAL); }
