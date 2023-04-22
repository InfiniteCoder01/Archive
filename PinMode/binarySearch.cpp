//#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int binarySearch(const vector<int>& vec, int target) { // I can use std::find, is better and unsorted vectors are supported!
	vector<int> data(vec);
	int increment = 0;
	if(!count(data.begin(), data.end(), target)) {
		return -1;
	}
	while(data.size() > 0) {
		int center = data.size() / 2;	
		if(data[center] == target) {
			return center + increment;
		} else if(data[center] < target) {
			data = vector<int>(vec.begin() + center, vec.end());
			increment += center;
		} else {
			data = vector<int>(vec.begin(), vec.end() - center - 1);
		}
	}
	return -1;
}

//int main() {
//	cout << binarySearch({-5,-3,-2}, -5) << " = 0\n";
//	cout << binarySearch({ }, 5) << " = -1\n";
//	cout << binarySearch({-10, -5, -4, 0, 9}, -4) << " = 2\n";
//	cout << binarySearch({-8,-5,-1,5,9,12}, 4) << " = -1\n";
//	cout << binarySearch({-5,-3,-2}, -5) << " = 0\n";
//}