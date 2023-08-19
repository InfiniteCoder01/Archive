#include <glm.hpp>
#include <mova.h>

using glm::vec2;

int random(int min, int max) { return rand() % (max - min) + min; }

int main() {
  MvWindow window = MvWindow("Mova sample");
  MvImage image = MvImage("test.png", false);
  float t = 0;
  vec2 pos = vec2(100), target = pos;
  MvFont font = MvFont("Consolas.ttf");
  Mova::setFont(font, 45);
  while (true) {
    if (Mova::isKeyPressed(MvKey::Space)) target = Mova::getViewportSize() / 2.f;
    if (pos != target) {
      pos = vec2(100) * (1 - t) + target * t;
      t = std::min(t + Mova::deltaTime() * 0.5f, 1.f);
    }
    Mova::clear(Color::white);
    vec2 center = Mova::getViewportSize() / 2.f;
    srand(1);
    for (int i = 0; i < 5; i++) {
      Mova::drawImage(image, center - image.size() / 2.f + pos * vec2(rand() % 2 * 2 - 1, rand() % 2 * 2 - 1) + vec2(random(-100, 100), random(-100, 100)));
    }
    Mova::nextFrame();
  }
}
