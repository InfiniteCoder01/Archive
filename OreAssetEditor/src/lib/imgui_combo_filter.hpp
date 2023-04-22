#pragma once

#include <imgui.h>
#include <imgui_internal.h>
#include <vector>
#include <string>

static bool ImGui_ComboFilter(const char* label, const std::vector<std::string>& items, int& selected) {  // https://github.com/ocornut/imgui/issues/718#issuecomment-578391838
  static bool isPopupOpen = false;
  static char buffer[256];

  ImGui::InputText(label, buffer, sizeof(buffer));
  bool isComboFocused = ImGui::IsItemFocused();
  if (ImGui::IsItemActive()) isPopupOpen = true;
  auto filterID = ImGui::GetItemID();

  ImGui::PushID(filterID);
  if (isPopupOpen) {
    ImGui::SetNextWindowPos({ImGui::GetItemRectMin().x, ImGui::GetItemRectMax().y});
    ImGui::SetNextWindowSize({ImGui::GetItemRectSize().x, 0});
    if (ImGui::Begin("##Popup", &isPopupOpen, ImGuiWindowFlags_NoTitleBar | ImGuiWindowFlags_NoMove | ImGuiWindowFlags_NoResize | ImGuiWindowFlags_Tooltip)) {
      ImGui::BringWindowToDisplayFront(ImGui::GetCurrentWindow());
      if (ImGui::IsWindowFocused()) isComboFocused = true;

      for (int i = 0; i < items.size(); i++) {
        if (strstr(items[i].c_str(), buffer) == NULL) continue;  // TODO: fuzzy

        bool isItemSelected = ImGui::Selectable(items[i].c_str());
        if (ImGui::GetFocusID() == filterID && ImGui::IsKeyPressedMap(ImGuiKey_DownArrow)) ImGui::SetFocusID(ImGui::GetItemID(), ImGui::GetCurrentWindow());
        if (isItemSelected || (ImGui::GetFocusID() == ImGui::GetItemID() && ImGui::IsKeyPressedMap(ImGuiKey_Enter))) {
          selected = i;
          isPopupOpen = false;
        }
      }
    }
    ImGui::End();
    if (!isComboFocused) isPopupOpen = false, selected = -1, isComboFocused = true;
  }
  ImGui::PopID();
  return isComboFocused && !isPopupOpen;
}
