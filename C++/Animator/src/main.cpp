#include "common.hpp"
#include "theme.hpp"
#include "render.hpp"

void renderViewport() {
  Mova::clear();
  for (auto node : animations) {
    do {
      if (inRange(time, node->time, node->next->time)) {
        float t = clamp((time - node->time) / (node->next->time - node->time), 0, 1);
        if (instanceof <WriteInAnimationNode>(node)) drawPaths(0, node->animate(t), t);
        else if (instanceof <WriteOutAnimationNode>(node)) drawPaths(0, node->animate(t), 1 - t);
        else drawPaths(0, node->animate(t));
      }
    } while (! instanceof <EndAnimationNode>(node = node->next));
  }
}

int main() {
  parseTTF("OpenSans.ttf");
  MvWindow window("Animator", Mova::OpenGL);
  animations.push_back(constructAnimation({
      new WriteInAnimationNode(0),
      new StandAnimationNode(parseSVG("M 60 10, Q 110 10 110 60, T 60 110, T 10 60, T 60 10"), 0.5),
      new MorphAnimationNode(1),
      new StandAnimationNode(parseSVG("M 10 10, L 110 10, L 110 110, L 10 110, Z").transformed(vec2i(300, 0), 2), 1.5),
      new WriteOutAnimationNode(3),
      new EndAnimationNode(4),
  }));

  ImGuiImplMova_Init();
  ImGui::GetIO().ConfigFlags |= ImGuiConfigFlags_DockingEnable;
  setTheme();
  while (window.isOpen) {
    ImGuiImplMova_NewFrame();
    ImGui::DockSpaceOverViewport(ImGui::GetMainViewport());

    {
      if (ImGui::Begin("Viewport", nullptr, ImGuiWindowFlags_MenuBar)) {
        if (ImGui::BeginMenuBar()) {
          ImGui::SetCursorPosX(ImGui::GetCursorPosX() + (ImGui::GetContentRegionAvail().x - ImGui::CalcTextSize("|>").x - ImGui::GetStyle().FramePadding.x * 2.0f) * 0.5);
          if (ImGui::Button("|>")) play = true;
          ImGui::EndMenuBar();
        }
        if (!viewport || viewport->size() != oreVec(ImGui::GetContentRegionAvail())) {
          viewport = std::unique_ptr<MvImage>(new MvImage(oreVec(ImGui::GetContentRegionAvail())));
        }
        Mova::setContext(*viewport);
        renderViewport();
        Mova::setContext(window);
        ImGui::Image((ImTextureID)(uintptr_t)(*viewport->asTexture()), ImGui::GetContentRegionAvail());
      }
      ImGui::End();
    }
    {
      static float timelineWidth = 5, timelineStart = 0;
      if (play) {
        time += Mova::deltaTime();
        if (time >= 5) time = 5, play = false;
      }
      if (ImGui::Begin("Timeline")) {
        ImGui::SetNextItemWidth(ImGui::GetWindowContentRegionWidth());
        if (ImGui::SliderFloat("##Time", &time, timelineStart, timelineStart + timelineWidth)) play = false;

        vec2i cursor = oreVec(ImGui::GetCursorScreenPos());
        vec2i timelineSize = oreVec(ImGui::GetContentRegionAvail()) - vec2i(15, 0);
        float timelinePosition = (Mova::getMousePos().x - cursor.x - ImGui::GetStyle().FramePadding.x) * timelineWidth / timelineSize.x;

        int id = 0;
        for (auto node : animations) {
          vec2i pos = vec2i((node->time - timelineStart) * timelineSize.x / timelineWidth + 3, 0);
          pos += cursor + oreVec(ImGui::GetStyle().FramePadding);
          ImGui::SetCursorScreenPos(imVec(pos));
          do {
            ImGui::Button(("##node" + std::to_string(id)).c_str(), ImVec2(5, 50));
            if (ImGui::IsItemActive()) node->move(timelinePosition);
            ImGui::SameLine(0, 0);
            if (! instanceof <EndAnimationNode>(node)) {
              ImGui::Button(getAnimationName(node).c_str(), ImVec2((node->next->time - node->time) * timelineSize.x / timelineWidth - 10, 50));
              ImGui::SameLine(0, 0);
            }
            id++;
          } while ((node = node->next));
        }

        vec2i timeCursor = cursor + oreVec(ImGui::GetStyle().FramePadding) + vec2i(timelineSize.x * (time - timelineStart) / timelineWidth + 3, -10);
        ImGui::GetWindowDrawList()->AddRectFilled(imVec(timeCursor), imVec(timeCursor + vec2i(3, timelineSize.y + 10)), MvColor::white.value, .5f);
      }
      ImGui::End();
    }

    Mova::clear();
    ImGuiImplMova_Render();
    Mova::nextFrame();
  }
  ImGuiImplMova_Shutdown();
  return 0;
}
