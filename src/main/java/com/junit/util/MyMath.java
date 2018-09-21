package com.junit.util;

import java.util.Arrays;

public class MyMath {


    public static void main(String[] args){

        int[] a1 = {1, 2, 3, 4, 5};
        int[] a2 = {1, 4, 6, 7, 8, 9, 99};
        a2 = Arrays.copyOf(a1, a1.length);
        a2[3] = 111;

        System.out.println(Arrays.toString(a1));// [1, 2, 3, 4, 5]
        System.out.println(Arrays.toString(a2)); // [1, 2, 3]


    }
    public int findLongest(String A, int n, String B, int m) {
        // write code here
        int[][] dp = new int[n][m];
        int max = 0;
        for (int i=0; i<n; i++) {
            for (int j=0; j<m; j++) {
                if (A.charAt(i) == B.charAt(j)) {
                    if (i==0 || j==0) {
                        dp[i][j] = 1;
                    } else {
                        dp[i][j] = dp[i-1][j-1] + 1;
                    }
                }
                max = max > dp[i][j] ? max : dp[i][j];
            }
        }

        return max;
    }

    public int add(int a, int b) {
        return a+b;
    }

    public int addPlus(int a, int b) {

        int ans = add(a, b) + 10;

        return ans;
    }

    public int addYou(int a, int b) {

        int ans = add(a, b);
        ans += YouMath.addLow();
        return ans;
    }

}
