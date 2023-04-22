#include "common.hpp"

PathList parseSVG(std::string _path) {
  std::replace(_path.begin(), _path.end(), ',', ' ');
  std::stringstream path = std::stringstream(_path);
  char command;
  vec2f pointer, tmp, c1, c2, tangentControl, first;
  bool firstCommand = true;
  std::vector<Bezier> rpath;
  while (path >> command) {
    if (toupper(command) == 'C') path >> c1.x >> c1.y >> c2.x >> c2.y >> tmp.x >> tmp.y;
    if (toupper(command) == 'S') path >> tangentControl.x >> tangentControl.y >> tmp.x >> tmp.y;
    if (toupper(command) == 'Q') path >> c1.x >> c1.y >> tmp.x >> tmp.y;
    if (strchr("MLT", toupper(command))) path >> tmp.x >> tmp.y;
    else if (toupper(command) == 'H') path >> tmp.x, tmp.y = isupper(command) ? pointer.y : 0;
    else if (toupper(command) == 'V') path >> tmp.y, tmp.x = isupper(command) ? pointer.x : 0;
    else if (toupper(command) == 'Z') tmp = first;
    if (islower(command)) tmp += pointer, c1 += pointer, c2 += pointer, tangentControl += pointer;
    if (firstCommand && strchr("LHV", toupper(command))) first = pointer, firstCommand = false;

    if (strchr("LHVZ", toupper(command))) {
      c1 = pointer + (tmp - pointer) / 3;
      c2 = pointer + (tmp - pointer) * 2 / 3;
    } else if (toupper(command) == 'S') {
      c1 = pointer * 2 - c2;
      c2 = tangentControl;
    } else if (toupper(command) == 'Q' || toupper(command) == 'T') {
      vec2i control = toupper(command) == 'T' ? pointer - (c2 - pointer) / 2 * 3 : c1;
      c1 = pointer + (control - pointer) * 2 / 3;
      c2 = tmp + (control - tmp) * 2 / 3;
    }
    if (command != 'M') rpath.push_back(Bezier{.a = pointer, .b = tmp, .c1 = c1, .c2 = c2});
    pointer = tmp;
  }
  return PathList{.paths = {rpath}};
}
