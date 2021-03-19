import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class Test {
	
	static int match_score = 1;
	static int mismatch_score = 0;
	static int gap = 0;
	static String v_last1 ="";	
	static String w_last1 ="";
	static int line = 0;
    static int sequence = 0;
    static int name_sequence=0;
    static String[] s = new String[6];
    static String[] sNames = new String[6];
	static int [][] blosum62 = new int[24][24];
	static String[] whichBlosumColumn = new String[24];
	public static void readFile(File inputFile) {
		BufferedReader brInput = null;
		try {
			brInput = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		String st; 
		  try {
			while ((st = brInput.readLine()) != null){ 
				  if(line%2==0) {
					  st=st.substring(1);
					  if(st.length()>5) {
						  sNames[name_sequence] = st.substring(0, 5);
					  }
					  else if(st.length()<5) {
						  for (int i = 0; i < 6-st.length(); i++) {
							st+=" ";
						}
						  sNames[name_sequence] = st;
					  }
					  else if((st.length()==5)) {
						  sNames[name_sequence] = st;
					  }
					  
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
	}
	public static void readBlosum() {
	
		File blosumFile = new File("./txt/Blosum62.txt"); 
  	  BufferedReader brBlosum = null;
		try {
			brBlosum = new BufferedReader(new FileReader(blosumFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		String st2; 
		int rowBlosum = -1;
		int columnBlosum = 0;
		int a = 0;
		  try {
			while ((st2 = brBlosum.readLine()) != null){ 
				  
				  String[] d2 = st2.split(" ");
				  if(rowBlosum<0) {
					  for (int i = 0; i < d2.length; i++) {
						if(d2[i].length()>0) {
							whichBlosumColumn[a]=d2[i];
							a++;
						}
					}
				  }
				  else {
					  for (int i = 0; i < d2.length; i++) {
							 if(d2[i].length()>0 && i>1){
								 blosum62[rowBlosum][columnBlosum] = Integer.valueOf(d2[i]);
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
		 
	}
	public static void printBlosumTable() {
    	
	    
		 System.out.print("    ");
		 for (int i = 0; i < whichBlosumColumn.length; i++) {
		     System.out.print(whichBlosumColumn[i]+"--|");
	     }
		 System.out.println();
		
		for (int i = 0; i < blosum62.length; i++) {
			System.out.print(whichBlosumColumn[i]+"--|");
			for (int j = 0; j < blosum62.length; j++) {
				if(blosum62[i][j]<0 || blosum62[i][j]>9) {
					System.out.print(blosum62[i][j]+" |");
				}
				else {
					System.out.print(blosum62[i][j]+"  |");
				}
				
			}
			System.out.println();
		}
   	
   }
	public static int getScoreBlosum(char val1,char val2){
		int scoreB= 0;
		
		 
		  int value1=0;
		  int value2=0;
		  for (int i = 0; i < whichBlosumColumn.length; i++) {
			
			  if(whichBlosumColumn[i].equals(String.valueOf(val1))){
				  value1=i;
			  }
			  if(whichBlosumColumn[i].equals(String.valueOf(val2))){
				  value2=i;
			  }
		}
		  
		scoreB = blosum62[value1][value2];
		//System.out.println(val1 +" and "+val2+" score:"+scoreB);
		
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
	public static void pairwiseAlignment(String v,String w,int[][] L,String[][] L2) {
		//System.out.println(v);
		//System.out.println(w);
		//System.out.println();
		
        L[0][0] = 0;
        L2[0][0]="*";
		
		for (int i = 0; i < L.length; i++) {
			for (int j = 0; j < L[0].length; j++) {
				if (i == 0 || j == 0){
                	if(i>0){
                		L[i][j] = L[i-1][j] + gap; 
                		 L2[i][j] = "|";
                	}
                	else if(j>0){
                		L[i][j] = L[i][j-1] + gap; 
                		L2[i][j] = "-";
                	}
                  
                } 
				else if (v.charAt(i-1) == w.charAt(j-1)) {
                	
					 match_score = getScoreBlosum(v.charAt(i-1), w.charAt(j-1));
	               	 L[i][j] = L[i-1][j-1] + match_score; 
	               	 L2[i][j] ="\\";
   	 
               }
			    else{	   
			    	    mismatch_score = getScoreBlosum(v.charAt(i-1), w.charAt(j-1));
	                	int max_vert_horiz=0;
	                	max_vert_horiz = Math.max(L[i-1][j], L[i][j-1]);
	                	L[i][j] = Math.max(L[i-1][j-1], max_vert_horiz) + mismatch_score; 
	                	
	                	if(L[i][j]==L[i-1][j]+ mismatch_score ) {
	                		L2[i][j]="|";
	                	}
	                	else if(L[i][j]==L[i][j-1]+ mismatch_score ) {
	                		L2[i][j]="-";
	                	}
	                	else if(L[i][j]==L[i-1][j-1]+ mismatch_score ) {
	                		L2[i][j] ="\\";
	                	}
			    }			
			}
		}
	}
	public static void printEditGraph(String v,String w,int[][] L) {
		System.out.println(" -"+w);
		for (int k = 0; k < L.length; k++) {
			if(k==0) {
				System.out.print("-");
			}
			if(k>0) {
				System.out.print(v.substring(k-1, k));
			}				
			for (int k2 = 0; k2 < L[0].length; k2++) {
				System.out.print(L[k][k2]);
			}
			System.out.println();
		}
		System.out.println();
	}
	public static void printEditGraphDirections(String v,String w,String[][] L2) {
		System.out.println(" -"+w);
		for (int k = 0; k < L2.length; k++) {
			if(k==0) {
				System.out.print("-");
			}
			if(k>0) {
				System.out.print(v.substring(k-1, k));
			}	
			for (int k2 = 0; k2 < L2[0].length; k2++) {
				System.out.print(L2[k][k2]);
			}
			System.out.println();
		}
		System.out.println();
	}
    public static void backtrack(String v,String w, String[][] L2) {
    
		int m = v.length(); 
        int n = w.length(); 	
		int i = L2.length-1;
		int j = L2[0].length-1;
		
		while(L2[i][j]!="*") {
			if(L2[i][j]=="-") {
				v_last1 += "_";
				w_last1 += w.charAt(n-1);
				j--;
				n--;
				
			}
			else if(L2[i][j]=="|") {
				v_last1 += v.charAt(m-1);
				w_last1 += "_";
				i--;
				m--;
			}
            else if(L2[i][j]=="\\") {
            	v_last1 += v.charAt(m-1);
		   	    w_last1 += w.charAt(n-1);
				i--;
				j--;
				m--;
				n--;
			}
            
		}
	
		v_last1 = reverse(v_last1);
		w_last1 = reverse(w_last1);
	}
    public static String scoreIdentical(String v, String w){
		double identical = 0.0;
        int similarity = 0;
		String similarityPosition="";
		for (int i = 0; i < v.length(); i++) {
			for (int j = 0; j < w.length(); j++) {
				if(i==j){
					if (v.substring(i, i+1).equalsIgnoreCase(w.substring(j, j+1))){
						similarity++;
						similarityPosition +="+";
					}
					else {
						similarityPosition +="-";
					}
				}
				
			}
		}
		int tot = v.length();
		identical = ((double)similarity / tot);
		String ide = String.format("%.4f", identical);
		ide = ide.substring(0,1)+"."+ide.substring(2,5);
		//System.out.println(similarityPosition);
		//System.out.println(similarity+"/"+tot+"  "+ide);		
		return ide;
	}
    public static void printSimilarityMatrix(String[][] similarityMatrix){
    	System.out.print("      ");
		for (int i = 0; i < similarityMatrix.length; i++) {
			System.out.print(sNames[i]+" ");
		}
		System.out.println();
		for (int i = 0; i < similarityMatrix.length; i++) {
			System.out.print(sNames[i]+" ");
			for (int j = 0; j < similarityMatrix.length; j++) {
				if(similarityMatrix[i][j]==null || similarityMatrix[i][j].equalsIgnoreCase("0.0") || similarityMatrix[i][j].equalsIgnoreCase("0,000")) {
					System.out.print("----- ");
				}
				else {
					System.out.print(similarityMatrix[i][j]+" ");
				}
				
			}
			System.out.println();
		}
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// step1
		
		// dosya okuma + 
		// blosum matrisi doldur +		
		// sequence leri doldur + 		
		// her pairin alignment ýný yap+		
		// gap penalty kullanýcýdan alýnacak+		
		// similarity score hesapla+
		// eþleþen / align edilmiþ uzunluk+		
		// similarity matrisi oluþtur+
		
		// step2
		
		// similarity matrise göre guide tree
		// neighbor joining kullan
		
		// step3
		
		
		// Scanner scan = new Scanner(System.in);
		
		// System.out.print("Enter Gap Penalty: ");
		// gap = scan.nextInt();
		
		System.out.println("Calculating for gap penalty is "+gap+" .....");
		
		 readBlosum();
		// printBlosumTable();
	
		File inputFile = new File("./txt/InputORG.txt");
		readFile(inputFile);
		
		/*for (int i = 0; i < sequence; i++) {
			System.out.println(sNames[i]+":"+s[i]);
		}*/
		System.out.println();
		String[][] similarityMatrix = new String[sequence][sequence];
		for (int i = 0; i < sequence; i++) {
			for (int j = 0; j < sequence; j++) {
				if(j>i) {
					String v = s[i];
					String w = s[j];
					int m = v.length(); 
			        int n = w.length(); 
					
					int[][] L = new int[m+1][n+1];
			        String[][] L2 = new String[m+1][n+1];
					
					pairwiseAlignment(v, w, L, L2);				
//					printEditGraph(v, w, L);					
//				    printEditGraphDirections(v, w, L2);					
					backtrack(v,w,L2);
					
//					System.out.println(v_last1);
//					System.out.println(w_last1);
					similarityMatrix[i][j]=scoreIdentical(v_last1, w_last1);
//					System.out.println();
					v_last1="";
					w_last1="";
				}
			}
		}
		printSimilarityMatrix(similarityMatrix);
		
		double[][] similarityMatrix2 = new double[sequence][sequence];
		for (int i = 0; i < similarityMatrix.length; i++) {
			for (int j = 0; j < similarityMatrix.length; j++) {
				if(j>i) {
					similarityMatrix2[i][j]=Double.parseDouble(similarityMatrix[i][j]);
				}
				
			}
		}
		
		
		int best_i = 0;
		int best_j = 0;
		
		double maxValue2 = similarityMatrix2[0][0];
		for(int i=0;i < similarityMatrix2.length;i++){
			 for (int j=1; j < similarityMatrix2[0].length; j++) {
				  if(similarityMatrix2[i][j] >= maxValue2){
					  maxValue2 = similarityMatrix2[i][j];
					  best_i = i;
					  best_j = j;
					  
					}
			}	    
		}
		String bests = best_i+","+best_j;
		System.out.println();
		System.out.println(bests);
		System.out.println();
		
		for (int i = 0; i < sequence; i++) {
			if(i>0) {
				sNames[i-1]=sNames[i];
			}
		}
		sNames[0]="X    ";
		
		double[][] similarityMatrix3 = new double[sequence-1][sequence-1];
		int k = 0;
		int k2 = 0;
		similarityMatrix3[0][0] = 0;
		for(int i=0;i < similarityMatrix2.length;i++){
			 for (int j=0; j < similarityMatrix2[0].length; j++) {
				  if(j>i) {
					  if(i==best_i) {
						  if(j!=best_j) {
							  double a = similarityMatrix2[i][j];
							  double b = similarityMatrix2[best_j][j];
							  double x = a+b;
							  similarityMatrix3[i][j-1] = x/2;
							  
						  }
					  }
					  else if (i!=best_j) {
							  double z = similarityMatrix2[i][j];
							  similarityMatrix3[i-1][j-1] = z/2;
					  }
				  }
				    
			}
			
		}
		System.out.println();
		String[][] similarityMatrix4 = new String[sequence-1][sequence-1];
		for (int i = 0; i < similarityMatrix3.length; i++) {
			for (int j = 0; j < similarityMatrix3.length; j++) {
				String ide = String.format("%.3f", similarityMatrix3[i][j]);
				similarityMatrix4[i][j] = ide;
			}
			
		}
		printSimilarityMatrix(similarityMatrix4);
		
		best_i = 0;
		best_j = 0;
		
		maxValue2 = similarityMatrix3[0][0];
		for(int i=0;i < similarityMatrix3.length;i++){
			 for (int j=1; j < similarityMatrix3[0].length; j++) {
				  if(similarityMatrix3[i][j] >= maxValue2){
					  maxValue2 = similarityMatrix3[i][j];
					  best_i = i;
					  best_j = j;
					  
					}
			}	    
		}
		String bests2 = best_i+","+best_j;
		System.out.println();
		System.out.println(bests2);
		System.out.println();
        
	}

}
