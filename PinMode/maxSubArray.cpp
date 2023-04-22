//#include <iostream>
#include <vector>
#include <stdlib.h>

using namespace std;

int maxSubArray(const vector<int>& arr) {
	int maxSum = INT_MIN, currentSum = 0;
	for(int n : arr) { // O(n)
        currentSum += n;
		maxSum = max(maxSum, currentSum);
        currentSum = max(currentSum, 0);
	}
	return maxSum;
}

//int main() {
//	cout << maxSubArray({-2, 1, -3, 4, -1, 2, 1, -5, 4}) << " = 6\n";
//	cout << maxSubArray({1}) << " = 1\n";
//	cout << maxSubArray({5, 4, -1, 7, 8}) << " = 23\n";
//	cout << maxSubArray({-5, -4, -3, -10}) << " = -3\n";
//}