/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangle;
import java.util.*;
import java.io.*;
/**
 *
 * @author willie
 */
public class Triangle {
    static List<int[]> trngl, solutions, tempe;
    
    /**
     * 
     * @param pos
     * @param row
     * @return maximum child
     */
    public static int max(int pos, int[] row){
        int posA = pos, posB = pos + 1;
        if(row[posA] > row[posB])
            return posA;
        else
            return posB;
    }
    
    /**
     * 
     * @param pos
     * @param row
     * @return maximum parent
     */
    public static int max_r(int pos, int[] row){
        int posA = pos, posB = pos - 1;
        if(posA == 0)
            return posA;
        if(pos == row.length)
            return posB;
        if(row[posA] > row[posB])
            return posA;
        else
            return posB;
    }
    
    public static int[] find_path(int posA, int posB, int rowA, int rowB, int sum){
        int[] ans = {sum, posA};
        if(rowB == rowA+1){
            if(posA == posB || posA == posB+1)
                return ans;
            else return null;
        }
        else if(rowA < rowB){
            int[] ansA , ansB;
            
            ansA = find_path(posA, posB, rowA+1, rowB, sum+trngl.get(rowA+1)[posA]);
            ansB = find_path(posA+1, posB, rowA+1, rowB, sum+trngl.get(rowA+1)[posA+1]);
            
            if(ansA == null){
                tempe.set(rowA+1, ansB);
                return ansB;
            }
            if(ansB == null){
                tempe.set(rowA+1, ansA);
                return ansA;
            }
            if(ansA[0] >= ansB[0]){
                tempe.set(rowA+1, ansA);
                return ansA;
            }
            else {
                tempe.set(rowA+1, ansB);
                return ansB;
            }
        }
        else return null;
        
    }
    
    public static int[] opt_rev(int pos){
        tempe.clear();
        tempe.addAll(solutions);
        int [] ans = new int[2], temp;
        int x = solutions.size();
        for(int i=x-1; i>0; i--){
            temp = find_path(solutions.get(i)[1], pos, i, x, solutions.get(i)[0]);
            if(temp != null){
                ans = temp;
                ans[1] = i;
                break;
            }
        }
        return ans;
    }
    
    /**
     * 
     * @param pos
     * @param row
     * @param one
     * @return sum
     */
    public static int reverse (int pos, int[] row, boolean one){
        int sum = row[pos], x= trngl.indexOf(row) - 1;
        while(x >= 0){
            
            if(pos == 0)
                sum += trngl.get(x)[pos];
            else if(pos == trngl.get(x).length){
                sum += trngl.get(x)[pos-1];
                pos = pos-1;
            }
            else{
                sum += Math.max(trngl.get(x)[pos], trngl.get(x)[pos-1]);
                pos = max_r(pos, trngl.get(x));
            }
            if(one)
                break;
            x--;
        }
        return sum;
    }
    
    
    /**
     * 
     * @param sum
     * @param pos
     * @param row
     * @return int[sum, pos, row]
     */
    public static int[] triangle_i(int sum, int pos, int[] row){
        int[] ans = new int[2];
        if(row.length == 1){
            ans[0] = sum += row[0];
            ans[1] = 0;
            return ans;
        }
        int mx = max(pos, row);
        int[] prev = trngl.get(trngl.indexOf(row)-1), tem;        
        for(int i=0; i<row.length; i++){
            if(row[i] > row[mx]){
                if(prev[pos] + row[mx] < reverse(i, row, true)){
                    tem = opt_rev(i);
                    if (tem[0]+row[i] > sum + row[mx]){
                        sum = tem[0];
                        solutions.retainAll(solutions.subList(0, tem[1]));
                        solutions.addAll(tempe.subList(tem[1], tempe.size()));
                        pos = solutions.get(solutions.size()-1)[1];
                        mx = i;
                    }
                }
            }
        }
        
        ans[0] = sum + row[mx];
        ans[1] = mx;
        return ans;
    }
    
    /**
     * 
     * @param strArr
     * @return int array
     */
    public static int[] string_to_int_array(String[] strArr){
        int[] intArr = new int[strArr.length];
        for(int i=0; i<strArr.length; i++){
            intArr[i] = Integer.parseInt(strArr[i]);
        }
        return intArr;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        trngl = new ArrayList();
        solutions = new ArrayList();
        tempe = new ArrayList();
        
        int[] ans = {0,0};
        String path = args[0].replace("\\", "\\\\");
        File tri = new File(path);
        try(BufferedReader Reader = new BufferedReader(new FileReader(tri))){
            String line;
            while((line = Reader.readLine())!=null){
                String[] temp = line.split(" ");
                int[] row = string_to_int_array(temp);
                trngl.add(row);
                ans = triangle_i(ans[0], ans[1], row);
                solutions.add(ans);
                tempe.add(ans);
            }
        }catch(IOException e){
            System.out.println(e);
        }
        System.out.println(ans[0]);
    }
    
    
}
