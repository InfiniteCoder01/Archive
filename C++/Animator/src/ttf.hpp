#include "common.hpp"

#define TTF_FONT_PARSER_IMPLEMENTATION
#include "ttfParser.h"

struct Letter {
  vec2f tl, size;
  PathList paths;
};

std::unordered_map<uint32_t, Letter> letters;

void parseTTF(std::string filename) {
  FILE* file = fopen(filename.c_str(), "rb");
  fseek(file, 0, SEEK_END);
  size_t size = ftell(file);
  fseek(file, 0, SEEK_SET);
  char* data = new char[size];
  fread(data, size, 1, file);
  fclose(file);
  TTFFontParser::FontData fontData;
  TTFFontParser::parse_data(data, &fontData);
  delete[] data;
  float avgCharWidth = 0, maxCharHeight = 1;
  for (const auto& glyph : fontData.glyphs) {
    letters[glyph.first] = Letter{
        .tl = vec2f(glyph.second.bounding_box[0], glyph.second.bounding_box[1]),
        .size = vec2f(glyph.second.bounding_box[2], glyph.second.bounding_box[3]),
    };
    maxCharHeight = Math::max(maxCharHeight, letters[glyph.first].tl.y + letters[glyph.first].size.y);
    avgCharWidth += letters[glyph.first].size.x;
    for (const auto& path : glyph.second.path_list) {
      std::vector<Bezier> rpath;
      for (const auto curve : path.geometry) {
        Bezier c = Bezier{.a = vec2f(curve.p0.x, curve.p0.y), .b = vec2f(curve.p1.x, curve.p1.y)};
        if (!curve.is_curve) {
          c.c1 = c.a + (c.b - c.a) / 3;
          c.c2 = c.a + (c.b - c.a) * 2 / 3;
        } else {
          c.b = vec2f(curve.c.x, curve.c.y);
          c.c1 = c.a + (vec2f(curve.p1.x, curve.p1.y) - c.a) * 2 / 3;
          c.c2 = c.b + (vec2f(curve.p1.x, curve.p1.y) - c.b) * 2 / 3;
        }
        rpath.push_back(c);
      }
      letters[glyph.first].paths.paths.push_back(rpath);
    }
  }
  avgCharWidth /= fontData.glyphs.size();
  for (const auto& glyph : fontData.glyphs) {
    letters[glyph.first].tl /= avgCharWidth;
    letters[glyph.first].size /= avgCharWidth;
    for (auto& path : letters[glyph.first].paths.paths) {
      for (auto& curve : path) {
        curve.a = vec2f(curve.a.x, maxCharHeight / 2 - curve.a.y) / avgCharWidth;
        curve.b = vec2f(curve.b.x, maxCharHeight / 2 - curve.b.y) / avgCharWidth;
        curve.c1 = vec2f(curve.c1.x, maxCharHeight / 2 - curve.c1.y) / avgCharWidth;
        curve.c2 = vec2f(curve.c2.x, maxCharHeight / 2 - curve.c2.y) / avgCharWidth;
      }
    }
  }
}
