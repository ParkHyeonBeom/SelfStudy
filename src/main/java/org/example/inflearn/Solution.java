package org.example.inflearn;

public class Solution {
    public int[] solution(int[] prices) {
        int[] answer = new int[prices.length];
        int[] a = prices;
        int count = 0;
        for(int i=0 ; i<a.length ; i++)
        {
            count=0;
            for(int j=i+1 ; j<a.length ; j++)
            if(a[i]<=a[j])
            {
                count++;
            }
            else if(a[i]>a[j])
            {
                count++;
                break;
            }
            answer[i]=count;
        }
            return answer;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int num[] = {1,2,3,2,3};
        solution.solution(num);
    }
}
