//#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int findPeak(const vector<int>& arr) {
	int start = 0, end = arr.size() - 1;

	while(start <= end){
		int center = start + (end - start) / 2;
		int middle = arr[center];
		int left = arr[center - 1];
		int right = arr[center + 1];
		if(middle > left) {
			if(middle > right) {
				return center;
			} else {
				start = center + 1;
			}
		} else {
			end = center - 1;
		}
	}
	return -1;
}

//int main() {
//	cout << findPeak({0, 1, 0}) << " = 1\n";
//	cout << findPeak({0, 1, 2, 0}) << " = 2\n";
//	cout << findPeak({0, 1, 2, 50, 60, 100, 1}) << " = 5\n";
//}