import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Test {

	static String [] m = new String[24]; // line of aminoacids
	static int [][] blosum62 = new int[24][24];
	
	
	static int chooseSimilarity = 0;
	//static ArrayList arlist = new ArrayList();// 
	
	static String w = "" ;
	static String v = "";
	static String tempw = ""; 
    static String tempv = "";
    static int fork1;  // fork [ means left and up direction possibilities
	static int fork2;  // fork < means diagonal and up direction possibilities
	static int fork3;  // fork > means left and diagonal direction possibilities
	static boolean printMatrices = false;
	
	static int line = 0;
    static int sequence = 0;
    static int name_sequence=0;
    
    static String[] s = new String[6];
    static String[] sNames = new String[6];
    static String[] newsNames = new String[6];
	
	static String[] tempS = new String[25];
	
	static ArrayList al = new ArrayList();
	
	//static int gap_penalty=0;
	
	public static void readInputFiles(File inputFile,File blosumFile) {
		
		BufferedReader brInput = null;
		try {
			brInput = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  BufferedReader brBlosum = null;
		try {
			brBlosum = new BufferedReader(new FileReader(blosumFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
		  String st; 
		  try {
			while ((st = brInput.readLine()) != null){ 
				  if(line%2==0) {
					  st=st.substring(1);
					  if(st.length()<6) {
						  int blank = 6-st.length();
						  for (int i = 0; i < blank; i++) {
							st+=" ";
						}
					  }
					  else if(st.length()>6){
						  st=st.substring(1,6);
					  }
					  
					  sNames[name_sequence] = st;
					  name_sequence++;
					  
				  }
				  if (line%2==1) {
					  //System.out.println(st);
					  s[sequence] = st;
					  sequence++;
				  }
				  line++;
					 
			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  int rowBlosum = -1;
		  int columnBlosum = 0;
		  
		  
		  int a = 0;
		  
		  String st2; 
		  try {
			while ((st2 = brBlosum.readLine()) != null){ 
				  
				  String[] d = st2.split(" ");
				  if (rowBlosum<0){
					  for (int i = 0; i < d.length; i++) {
							 if(!d[i].equals("")){
								 //System.out.print(d[i]+" ");
								 m[a]=d[i];
								 a++;
							 }
						} 
				  }
				  else{
					  for (int i = 0; i < d.length; i++) {
							 if(!d[i].equals("") && i>0){
								 //System.out.print(Math.abs(Integer.valueOf(d[i]))+" ");
								 blosum62[rowBlosum][columnBlosum] = Integer.valueOf(d[i]);
								 columnBlosum++;
							 }
						} 
				  }
				  
				  //System.out.println();
				  rowBlosum++;	
				  columnBlosum= 0;
			  }
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  
		  try {
			brInput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  try {
			brBlosum.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) throws IOException {
	
		
		File file = new File("./txt/InputORG.txt"); 
		File file2 = new File("./txt/Blosum62.txt"); 
		
		readInputFiles(file, file2);
		
		//////////////////////////////////////////////////////////////////////////////////////////////
		  
	       
	    	 for (int i = 0; i < sequence; i++) {
				for (int j = 0; j < sequence; j++) {
					if(j>i){
						 
						 calculate(sNames[i],sNames[j],s[i],s[j]);
						 
					}
				}
			}
	    	 
	    	 
	    	 
	    		  
		//////////////////////////////////////////////////////////////////////////  
		  
		///////////////////similarity matrix alignmentlara göre doldurulacak
		  //////////// birden fazla ihtimal varsa ilk ihtimali al
		
		double[][] similarityMatrix = new double[sequence][sequence];
		
		int x=0;
		int y=1;
		
		for (int i = 0; i < similarityMatrix.length; i++) {
			for (int j = 0; j < similarityMatrix[0].length; j++) {
				if(i==j){
					similarityMatrix[i][j]=0;
					
				}
				else if(j>i){					
					similarityMatrix[i][j]=scoreIdentical(tempS[x],tempS[y]);////??
					x=x+2;
					y=y+2;
				}
			
			}
		}
	
		System.out.println();
		System.out.print("       ");
		int c = 0;
		while(sNames[c]!=null) {		
			System.out.print(sNames[c]);		
			c++;
		}
		System.out.println();
		for (int i = 0; i < similarityMatrix.length; i++) {			
			System.out.print(sNames[i]+" ");		
			for (int j = 0; j < similarityMatrix[0].length; j++) {
				if(i==j || similarityMatrix[i][j]==0) {
					System.out.print("----- ");
				}
				else {
					String fr = String.format("%.3f",similarityMatrix[i][j]);
					System.out.print(fr+" ");
				}
			}
			System.out.println();
			
		}
		  
		 System.out.println();
		 
		 
		///////////////////////guide tree
		String guide="(";
		
		
		int best_i = 0;
		int best_j = 0;
		
		double maxValue2 = similarityMatrix[0][0];
		for(int i=0;i < similarityMatrix.length;i++){
			 for (int j=1; j < similarityMatrix[0].length; j++) {
				  if(similarityMatrix[i][j] >= maxValue2){
					  maxValue2 = similarityMatrix[i][j];
					  best_i = i;
					  best_j = j;
					  
					}
			}	    
		}
		String bests = sNames[best_i].substring(0, 2)+","+sNames[best_j].substring(0, 2);
		guide += bests;
		guide += ")";
		System.out.println(guide);
		
		
		
		
		 
		 
		 
		 
		 
		 
		 
		 
	
	}
	

	public static double scoreIdentical(String s, String m){
		double identical = 0.0;
		
        int similarity = 0;
		
		for (int i = 0; i < s.length(); i++) {
			for (int j = 0; j < m.length(); j++) {
				if(s.substring(i, i+1).equalsIgnoreCase(m.substring(j, j+1))){
					if (i==j){
						similarity++;
					}
				}
				
			}
		}
		int tot = s.length();
		identical = ((double)similarity / tot);
		String ide = String.format("%.3f", identical);
		ide = ide.substring(0,1)+"."+ide.substring(2);
		//System.out.println(ide);
		double percent = Double.parseDouble(ide);
		//System.out.println(percent);
		//System.out.println("similarity: "+similarity+"/"+tot+" %"+String.format("%.2f",identical)+" identical");
		return percent;
	}
	
	public static int getScoreBlosum(char val1,char val2){
		int scoreB= 0;
		
		 
		  int value1=0;
		  int value2=0;
		  for (int i = 0; i < m.length; i++) {
			
			  if(m[i].equals(String.valueOf(val1))){
				  value1=i;
			  }
			  if(m[i].equals(String.valueOf(val2))){
				  value2=i;
			  }
		}
		  
		scoreB = blosum62[value1][value2];
		 
		//System.out.println("score:"+scoreB);
		
		return scoreB;
	}
	
	public static String reverse(String str){
    	String rev = "";
    	
    	for(int i = str.length() - 1; i >= 0; i--)
        {
            rev = rev + str.charAt(i);
        }
    	
    	return rev;
    }
	
	public static void lcs(String V, String W) 
	    { 
	    	
	    	
	    	int m = V.length(); 
	        int n = W.length(); 
	        
	        boolean fork = false;
	        
	        int[][] L = new int[m+1][n+1];
	        String[][] L2 = new String[m+1][n+1];
	        L[0][0] = 0;
	        
	        
	 
	       
	        for (int i=0; i<=m; i++) 
	        { 
	            for (int j=0; j<=n; j++) 
	            { 
	                if (i == 0 || j == 0){
	                	if(i>0){
	                		L[i][j] = L[i-1][j]; // + (-1);// gap penalty -1
	                		 L2[i][j] = "|";
	                	}
	                	else if(j>0){
	                		L[i][j] = L[i][j-1]; //+ (-1);// gap penalty -1
	                		L2[i][j] = "-";
	                	}
	                	else{
	                		L2[i][j] = "*";
	                	}
	                   
	                }     
	                else if (V.charAt(i-1) == W.charAt(j-1)) {
	                	
	                	 int matchScore= getScoreBlosum(V.charAt(i-1),W.charAt(j-1));
	                	 L[i][j] = L[i-1][j-1] + matchScore; //match score
	                	 // \
	                	 // yukarýdan ve soldan gelenlere gap penalty uygula
	                	 if(L[i][j]==L[i-1][j]){
	                		 L2[i][j] ="<";
	                	 }
	                	 else if(L[i][j]==L[i][j-1]){
	                		 L2[i][j] =">";
	                	 }
	                	 else{
	                		 L2[i][j] ="\\";
	                	 }
	                	 
	                }
	                   
	                else{
	                	// yukarýdan ve soldan gelenlere gap penalty uygula
	                	
	                	
	                	
	                	L[i][j] = Math.max(L[i-1][j], L[i][j-1]); 
	                	if(L[i-1][j]>L[i][j-1]){
	                		//   |
	                		L2[i][j]="|";
	                	}
	                	else if(L[i-1][j]<L[i][j-1]){
	                		//   
	                		L2[i][j]="-";
	                	}
	                    else{
	                    	if(L[i-1][j]==L[i][j-1] && L[i-1][j]==L[i][j]){
	                    	
	                    			L2[i][j]="[";
	                    		
	                    	}
	                    	else if (L[i-1][j]==(L[i-1][j-1]) && L[i-1][j]==L[i][j]){
	                    		L2[i][j]="<";
	                    	}
	                        else if (L[i][j-1]==(L[i-1][j-1]) && L[i][j-1]==L[i][j]){
	                        	L2[i][j]=">";
	                    	}
	                		
	                    	//L2[i][j]="-";
	                	}
	                }
	                    
	            } 
	        } 
	        if(printMatrices){
	        	
	        	System.out.println("\n -"+W);
	            for (int i=0; i<=m; i++) {
	            	if(i<1){
	            		System.out.print("-");
	            	}
	            	else{
	            		System.out.print(V.substring(i-1,i));
	            	}
	                for (int j=0; j<=n; j++) { 
	                   System.out.print(L[i][j]);
	                } 
	                System.out.println();
	            } 
	            System.out.println("\n -"+W);
	            for (int i=0; i<=m; i++) { 
	            	if(i<1){
	            		System.out.print("-");
	            	}
	            	else{
	            		System.out.print(V.substring(i-1,i));
	            	}
	                for (int j=0; j<=n; j++) { 
	                   System.out.print(L2[i][j]);
	                } 
	                System.out.println();
	            } 
	            System.out.println();
	        }
	        
	        
	        
	       
	        int index = L[m][n]; // alignment score
	       // int temp = index; 
	   
	       
	        char[] lcs = new char[index+1]; 
	        lcs[index] = ' '; 
	   
	      
	        w="";
	        v="";
	        int i = m, j = n; 
	        while (i > 0 && j > 0 && !fork) 
	        {
	            if (V.charAt(i-1) == W.charAt(j-1)) 
	            { 
	          
	                 if (L2[i][j].equalsIgnoreCase(">")){
	            		
	                	// sola ve çapraza gidebilir
	            		// dallanma
	            	//	System.out.print("fork >");
	            		fork = true;
	            		if(fork){
	            			if(fork3==0){
	            				v +=  "-";
	                    	    w +=  W.charAt(j-1);
	                    	    j--;
	            			}
	            			else{
	            				w +=  W.charAt(j-1);
	                            v +=  V.charAt(i-1); 
	                        	i--;
	                            j--;
	                            
	            			}
	            			//System.out.println("i:"+i);
	            			//System.out.println("j:"+j);
	            			fork=false;
	            		}
	                	  
	                }
	            	else if (L2[i][j].equalsIgnoreCase("<")){
	            		
	                	// yukarý ve çapraza gidebilir
	            		//dallanma
	            	//	System.out.print("fork <");
	            		fork = true;
	            		if(fork){
	            			if(fork2==0){ //yukarý
	            			//	System.out.println("go |");
	            				 v += V.charAt(i-1); 
	                         	 w += "-";
	                             i--;
	            			}
	            			else{// diagonal
	            			//	System.out.println("go \\");
	            				w +=  W.charAt(j-1);
	                            v +=  V.charAt(i-1); 
	                        	i--;
	                            j--;
	            			}
	            			//System.out.println("i:"+i);
	            			//System.out.println("j:"+j);

	            			fork=false;
	            		}
	                	  
	                }
	            	else{
	            	//	System.out.println("go \\");
	            		//lcs[index-1] = X.charAt(i-1);  
	            		w +=  W.charAt(j-1);
	                    v +=  V.charAt(i-1); 
	                    i--;
	                    j--;
	                    //System.out.println("i:"+i);
	        			//System.out.println("j:"+j);
	            	}
	                
	                // reduce values of i, j and index 
	               // i--;  
	               // j--;  
	                index--;      
	            } 
	        
	            else{
	            	if(L2[i][j].equalsIgnoreCase("-")){
	            		// sola gidebilir
	            	//	System.out.println("go -");
	            		v +=  "-";
	            	    w +=  W.charAt(j-1);
	                    j--;
	                    //System.out.println("i:"+i);
	        			//System.out.println("j:"+j);
	            	}
	            	else if (L2[i][j].equalsIgnoreCase("|")){
	            	//	System.out.println("go |");
	            	   // yukarý gidebilir
	            	    v += V.charAt(i-1); 
	                	w += "-";
	                    i--;
	                    //System.out.println("i:"+i);
	        			//System.out.println("j:"+j);
	            	}
	            	
	            	else if (L2[i][j].equalsIgnoreCase("[")){
	            		
	                	// sola ve yukarý  gidebilir
	            		//dallanma
	            	//	System.out.print("fork [");
	            		fork = true;
	            		if(fork){
	            			if(fork1==0){
	            		//		System.out.println("go -");
	            				v +=  "-";
	                    	    w +=  W.charAt(j-1);
	                    	    j--;
	                    	    
	            			}
	            			else{
	            		//		System.out.println("go |");
	            				v += V.charAt(i-1); 
	                        	w += "-";
	                        	i--;
	                            
	            			}
	            			//System.out.println("i:"+i);
	            			//System.out.println("j:"+j);
	            			
	                        
	            			fork=false;
	            		}
	            		
	                	  
	                }
	            	
	            }
	            	 
	        } 
	         
	       
	            
	    } 
	
	public static void calculate(String s1Name, String s2Name, String s1, String s2){
		v=s1;
    	w=s2;
    	
    	
    	System.out.println("-------------------");
    	String result;
    	
    	System.out.println(s1Name+":"+v);
        System.out.println(s2Name+":"+w);
        
        tempv=v;
        tempw=w;
        System.out.println("-------------------");
        fork1=0;fork2=0;fork3=0;
       // printMatrices=true;
        lcs(v, w); 
        printMatrices=false;
  //      System.out.println();
   
        tempS[chooseSimilarity]=reverse(v);
        chooseSimilarity++;
        tempS[chooseSimilarity]=reverse(w);
        chooseSimilarity++;
        
        result=s1Name+":"+reverse(v)+"\n"+s2Name+":"+reverse(w);
        
   //     System.out.println(result); //
        
        if(!al.contains(result)){
        	al.add(result);
        	
		}
        result="";
        
        v="";
        w="";
        
        v=tempv;
        w=tempw;
        
        fork1=0;fork2=0;fork3=1;
        lcs(v, w); 
   //     System.out.println();
   
        result=s1Name+":"+reverse(v)+"\n"+s2Name+":"+reverse(w);
    //    System.out.println(result); //
        
        if(!al.contains(result)){
        	al.add(result);
        	
		}
        result="";
        
        v="";
        w="";
        
        v=tempv;
        w=tempw;
        
        fork1=0;fork2=1;fork3=0;
        lcs(v, w); 
   //     System.out.println();
   
        result=s1Name+":"+reverse(v)+"\n"+s2Name+":"+reverse(w);
     //   System.out.println(result); //
        
        if(!al.contains(result)){
        	al.add(result);
        	
		}
        result="";
        
        v="";
        w="";
        
        v=tempv;
        w=tempw;
        
        fork1=0;fork2=1;fork3=1;
        lcs(v, w); 
   //     System.out.println();
   
        result=s1Name+":"+reverse(v)+"\n"+s2Name+":"+reverse(w);
     //   System.out.println(result); //
        
        if(!al.contains(result)){
        	al.add(result);
        	
		}
        result="";
       
        v="";
        w="";
        
        v=tempv;
        w=tempw;
        
        fork1=1;fork2=0;fork3=0;
        lcs(v, w); 
      //  System.out.println();
   
        result=s1Name+":"+reverse(v)+"\n"+s2Name+":"+reverse(w);
      //  System.out.println(result); //
        
        if(!al.contains(result)){
        	al.add(result);
        	
		}
        result="";
        
        v="";
        w="";
        
        v=tempv;
        w=tempw;
        
        fork1=1;fork2=0;fork3=1;
        lcs(v, w); 
   //     System.out.println();
   
        result=s1Name+":"+reverse(v)+"\n"+s2Name+":"+reverse(w);
     //   System.out.println(result); //
        
        if(!al.contains(result)){
        	al.add(result);
        	
		}
        result="";
        
        v="";
        w="";
        
        v=tempv;
        w=tempw;
        
        fork1=1;fork2=1;fork3=0;
        lcs(v, w); 
   //     System.out.println();
   
        result=s1Name+":"+reverse(v)+"\n"+s2Name+":"+reverse(w);
      //  System.out.println(result); //
        
        if(!al.contains(result)){
        	al.add(result);
        	
		}
        result="";
        
        v="";
        w="";
        
        v=tempv;
        w=tempw;
        
        fork1=1;fork2=1;fork3=1;
        lcs(v, w); 
   //     System.out.println();
   
        result=s1Name+":"+reverse(v)+"\n"+s2Name+":"+reverse(w);
       // System.out.println(result); //
        
        if(!al.contains(result)){
        	al.add(result);
        	
		}
        result="";
        System.out.println("********");
        for (int i = 0; i < al.size(); i++) {
			System.out.println(al.get(i));
			System.out.println();
		}
        System.out.println("}}}}}}}}}}}}}}}}");
        al.removeAll(al);
		
	}

}
