//#include <iostream>
//#include <iterator>
//#include <sstream>
#include <stdlib.h>
#include <vector>

using namespace std;

void selectionSort(vector<int>& arr) {
	if(arr.empty()) {
		return;
	}
	unsigned long start = 0;
	while(start < arr.size() - 1) {
		int minElement = INT_MAX;
		unsigned long minIndex = 0;
		for(unsigned long i = start; i < arr.size(); i++) {
			if(arr[i] < minElement) {
				minElement = arr[i];
				minIndex = i;
			}
		}
		arr[minIndex] = arr[start];
		arr[start] = minElement;
		start++;
	}
}

//// Just to test:
//string vectostring(vector<int> vec) {
//	if(vec.empty()) {
//		return "{}";
//	}
//	stringstream oss;
//	copy(vec.begin(), vec.end() - 1, ostream_iterator<int>(oss, ","));
//	oss << vec.back();
//	return "{" + oss.str() + "}";
//}
//
//int main() {
//	vector<int> arr = {5, 10, 8, 1, 4, 3, 1};
//	selectionSort(arr);
//	cout << vectostring(arr) << " = {1, 1, 3, 4, 5, 8, 10}\n";
//	arr = {20, 10};
//	selectionSort(arr);
//	cout << vectostring(arr) << " = {10, 20}\n";
//	arr = {3, 3, 3, 3, 2};
//	selectionSort(arr);
//	cout << vectostring(arr) << " = {2, 3, 3, 3, 3}\n";
//	arr = {1};
//	selectionSort(arr);
//	cout << vectostring(arr) << " = {1}\n";
//	arr = {};
//	selectionSort(arr);
//	cout << vectostring(arr) << " = {}\n";
//	arr = {5, 10, 5};
//	selectionSort(arr);
//	cout << vectostring(arr) << " = {5, 5, 10}\n";
//}