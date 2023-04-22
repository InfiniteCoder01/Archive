#include <mova.h>
#include <thread>
#include <chrono>
#include <queue>

using namespace Math;
using namespace VectorMath;

void solveMaze(MvImage* map) {
  vec2i start, finish;
  uint32_t* distances = new uint32_t[map->width * map->height];
  const uint32_t MAX_DISTANCE = 0xffffffffUL, WALL_DISTANCE = 0xfffffffeUL;
  const vec2i DIRECTIONS[] = {vec2i(1, 0), vec2i(-1, 0), vec2i(0, 1), vec2i(0, -1)};

  // Scan image
  for (uint32_t x = 0; x < map->width; x++) {
    for (uint32_t y = 0; y < map->height; y++) {
      MvColor pixel = map->getPixel(x, y);
      if (pixel.b > 200 && pixel.g < 200) start = vec2i(x, y);
      else if (pixel.r > 200 && pixel.g < 200) finish = vec2i(x, y);
      distances[x + y * map->width] = (pixel.r > 200 && pixel.g < 200) ? 0 : (pixel.r < 200 && pixel.g < 200 && pixel.b < 200 ? WALL_DISTANCE : MAX_DISTANCE);
    }
  }

  // Flood Fill Distances
  std::queue<vec2i> q;
  q.push(finish);
  while (!q.empty()) {
    vec2i cell = q.front();
    q.pop();
    if(cell == start) break;

    for (const auto dir : DIRECTIONS) {
      if (distances[cell.x + dir.x + (cell.y + dir.y) * map->width] == MAX_DISTANCE) {
        distances[cell.x + dir.x + (cell.y + dir.y) * map->width] = distances[cell.x + cell.y * map->width] + 1;
        map->fillRect(cell, 1, MvColor::green);
        q.push(cell + dir);
      }
    }
    std::this_thread::sleep_for(std::chrono::microseconds(10));
  }

  // Find Path
  vec2i cell = start;
  while(cell != finish) {
    map->fillRect(cell - 1, 3, MvColor::red);

    vec2i best;
    uint32_t bestDistance = MAX_DISTANCE;
    for (const auto dir : DIRECTIONS) {
      if (distances[cell.x + dir.x + (cell.y + dir.y) * map->width] < bestDistance) {
        bestDistance = distances[cell.x + dir.x + (cell.y + dir.y) * map->width];
        best = cell + dir;
      }
    }
    cell = best;
    std::this_thread::sleep_for(std::chrono::microseconds(100));
  }
}

int main() {
  MvWindow window("Test");
  MvImage map("map.png");

  std::thread t1(solveMaze, &map);
  while (window.isOpen) {
    window.setSize(map.width * 0.9f, map.height * 0.9f);
    window.setPosition(window.getPosition().x, 20);
    window.drawImage(map, 0, 0, window.width, window.height);
    Mova::nextFrame();
  }
  return 0;
}
