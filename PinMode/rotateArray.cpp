//#include <iostream>
//#include <iterator>
//#include <sstream>
#include <vector>

using namespace std;

void rotateArray(vector<int>& arr, int k) {
	for(; k > 0; k--) {
		arr.reserve(1);
		arr.insert(arr.begin(), arr.rbegin()[0]);
		arr.pop_back();
	}
}

//// Just to test:
//string vectostring(vector<int> vec) {
//	stringstream oss;
//	copy(vec.begin(), vec.end() - 1, ostream_iterator<int>(oss, ","));
//	oss << vec.back();
//	return oss.str();
//}
//
//int main() {
//	vector<int> arr = {-1, -100, 3, 99};
//	rotateArray(arr, 2);
//	cout << vectostring(arr) << " = {3, 99, -1, -100}\n";
//	arr = {1, 2, 3, 4, 5, 6, 7};
//	rotateArray(arr, 3);
//	cout << vectostring(arr) << " = {5, 6, 7, 1, 2, 3, 4}\n";
//	arr = {1, 2};
//	rotateArray(arr, 4);
//	cout << vectostring(arr) << " = {1, 2}\n";
//}