#pragma once
#include <mova.h>
#include <renderer.h>
#include <imGuiImplMova.hpp>
#include <algorithm>
#include <string>
#include <sstream>
#include <unordered_map>
#include <stdio.h>

using namespace Math;
using namespace VectorMath;

inline vec2f oreVec(ImVec2 v) { return vec2f(v.x, v.y); }
inline ImVec2 imVec(vec2f v) { return ImVec2(v.x, v.y); }
template <typename T> static T fread(FILE* file) {
  T t;
  fread(&t, sizeof(t), 1, file);
  return t;
}

template <typename Base, typename T> inline bool instanceof (const T* ptr) { return dynamic_cast<const Base*>(ptr) != nullptr; }

struct Bezier {
  vec2f a, b, c1, c2;
  float len = 0;

  Bezier translated(vec2i pos, float width) const {
    return Bezier{
        .a = a * width + pos,
        .b = b * width + pos,
        .c1 = c1 * width + pos,
        .c2 = c2 * width + pos,
    };
  }

  vec2f point(float t) const {
    vec2f ac1 = lerp(a, c1, t), c2b = lerp(c2, b, t), c1c2 = lerp(c1, c2, t);
    vec2f ac1c2 = lerp(ac1, c1c2, t), c1c2b = lerp(c1c2, c2b, t);
    return lerp(ac1c2, c1c2b, t);
  }

  float length() {
    if (len == 0) {
      float step = 1.f / (distance(a, c1) + distance(c1, c2) + distance(c2, b));
      vec2f last = a;
      for (float t = step; t <= 1; t += step) {
        vec2f p = point(t);
        len += distance(last, p);
        last = p;
      }
    }
    return len;
  }
};

struct PathList {
  std::vector<std::vector<Bezier>> paths;

  PathList transformed(vec2i pos, float width) const {
    PathList other;
    other.add(pos, width, *this);
    return other;
  }

  void add(vec2i pos, float width, const PathList& other) {
    for (const auto& path : other.paths) {
      std::vector<Bezier> newPath;
      for (const auto& curve : path) {
        newPath.push_back(curve.translated(pos, width));
      }
      paths.push_back(newPath);
    }
  }
};

class AnimationNode {
 public:
  float time = 0;
  AnimationNode *next = nullptr, *prev = nullptr;
  AnimationNode(float time) : time(time) {}

  void move(float _time);
  virtual PathList animate(float t) = 0;
};

class StandAnimationNode : public AnimationNode {
 public:
  PathList pathes;
  StandAnimationNode(PathList pathes, float time) : pathes(pathes), AnimationNode(time) {}

  virtual PathList animate(float t) override { return pathes; }
};

class EndAnimationNode : public AnimationNode {
 public:
  EndAnimationNode(float time) : AnimationNode(time) {}

  virtual PathList animate(float t) override { return {}; }
};

class WriteInAnimationNode : public AnimationNode {
 public:
  WriteInAnimationNode(float time) : AnimationNode(time) {}

  virtual PathList animate(float t) override { return next->animate(0); }
};

class WriteOutAnimationNode : public AnimationNode {
 public:
  WriteOutAnimationNode(float time) : AnimationNode(time) {}

  virtual PathList animate(float t) override { return prev->animate(1); }
};

class MorphAnimationNode : public AnimationNode {
 public:
  MorphAnimationNode(float time) : AnimationNode(time) {}

  void extendPaths(PathList& a, PathList& b) {
    int bPathCount = b.paths.size();
    while (a.paths.size() > b.paths.size()) {
      for (int i = 0; i < min(a.paths.size() - b.paths.size(), bPathCount); i++) {
        b.paths.push_back(b.paths[i]);
      }
    }
  }

  void extendCurves(std::vector<Bezier>& a, std::vector<Bezier>& b) {
    int bCurveCount = b.size();
    while (a.size() > b.size()) {
      for (int i = 0; i < min(a.size() - b.size(), bCurveCount); i++) {
        b.push_back(b[i]);
      }
    }
  }

  virtual PathList animate(float t) override {
    PathList from = prev->animate(1);
    PathList to = next->animate(0);
    extendPaths(from, to);
    extendPaths(to, from);
    for (int i = 0; i < from.paths.size(); i++) {
      extendCurves(from.paths[i], to.paths[i]);
      extendCurves(to.paths[i], from.paths[i]);
      for (int j = 0; j < from.paths[i].size(); j++) {
        from.paths[i][j].a = lerp(from.paths[i][j].a, to.paths[i][j].a, t);
        from.paths[i][j].b = lerp(from.paths[i][j].b, to.paths[i][j].b, t);
        from.paths[i][j].c1 = lerp(from.paths[i][j].c1, to.paths[i][j].c1, t);
        from.paths[i][j].c2 = lerp(from.paths[i][j].c2, to.paths[i][j].c2, t);
      }
    }
    return from;
  }
};

AnimationNode* constructAnimation(std::vector<AnimationNode*> nodes) {
  AnimationNode* node = nodes[0];
  for (int i = 1; i < nodes.size(); i++) {
    node = node->next = nodes[i];
    node->prev = nodes[i - 1];
  }
  return nodes[0];
}

std::string getAnimationName(AnimationNode* node) {
  if (instanceof <StandAnimationNode>(node)) return "Stand";
  else if (instanceof <WriteInAnimationNode>(node)) return "Write In";
  else if (instanceof <WriteOutAnimationNode>(node)) return "Write Out";
  else if (instanceof <MorphAnimationNode>(node)) return "Morph";
  return "Unknown";
}
