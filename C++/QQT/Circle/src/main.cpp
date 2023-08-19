#include <glm.hpp>
#include <mova.h>

using glm::vec2;

int main() {
  MvWindow window = MvWindow("Mova sample");
  MvImage image = MvImage("test.png", false);
  float t = 0;
  float target = t;
  MvFont font = MvFont("Consolas.ttf");
  Mova::setFont(font, 45);
  while (true) {
    if (Mova::isKeyPressed(MvKey::Space) && t == target) target = t + 6.283f / 5;
    if (t < target) t += Mova::deltaTime() * 3.1415;
    else t = target;
    Mova::clear(Color::white);
    vec2 center = Mova::getViewportSize() / 2.f;
    for (float i = 0; i < 6.283f; i += 6.283f / 5) {
      Mova::drawImage(image, center - image.size() / 2.f + vec2(sin(i), cos(i)) * 200.f);
      Mova::drawImage(image, center - image.size() / 2.f + vec2(sin(i + t), cos(i + t)) * 300.f);
    }
    Mova::drawText(center - Mova::textSize("Name") / 2.f, "Name", Color::black);
    Mova::nextFrame();
  }
}
