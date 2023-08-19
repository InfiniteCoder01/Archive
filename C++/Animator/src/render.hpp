#include "common.hpp"
#include "ttf.hpp"
#include "svg.hpp"

std::unique_ptr<MvImage> viewport;
std::vector<AnimationNode*> animations;
float time;
bool play = false;

void drawPath(vec2i pos, std::vector<Bezier>& path, float T) {
  if (T == 0) return;
  {
    float len = 0;
    for (auto& curve : path) len += curve.length();
    T *= len;
  }
  for (auto& curve : path) {
    for (float t = 0; t <= min(T / curve.length(), 1); t += 1 / curve.length()) {
      Mova::fillRect(curve.point(t) + pos - 1, 3, MvColor::white);
    }
    T -= curve.length();
  }
  // for (int y = tl.y; y <= br.y; y++) {
  //   uint16_t nLines = 0;
  //   for (int x = tl.x; x <= br.x; x++) {
  //     if (infill[x - tl.x + (y - tl.y) * infillWidth]) nLines++;
  //     else if (nLines % 2 != 0) viewport->setPixel(vec2i(x, y), MvColor::gray);
  //   }
  // }
}

void drawPaths(vec2i pos, PathList paths, float T = 1) {
  T *= paths.paths.size();
  for (auto& path : paths.paths) {
    if (T < 0) break;
    drawPath(pos, path, T);
    T--;
  }
}

PathList constructText(std::string text, int letterW) {
  PathList paths;
  vec2i pointer = 0;
  for (auto c : text) {
    paths.add(pointer, letterW, letters[c].paths);
    pointer.x += (letters[c].tl.x + letters[c].size.x) * letterW;
  }
  return paths;
}

void AnimationNode::move(float _time) { time = _time; }
