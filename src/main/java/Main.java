import RDF.RDF_Management;
import Elements.Legal_Element;
import java.io.File; 
import java.util.Scanner;

public class Main{
	public static void main(String[] args){
		Scanner scanner=new Scanner(System.in);
		while (true){
			String choice=""; 

			// Checking for parameters
			if(args.length>0 && args[0].equalsIgnoreCase("create")){
				choice="Yes";
			}else if(args.length>0 && args[0].equalsIgnoreCase("load")){
				choice="No"; 
			} 
			System.out.println("Welcome!\n"); 
			while (true){ 
				System.out.println("Do you want to create a new RDF model? (Yes/No)");
				choice=scanner.nextLine();
				if(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("No") || choice.equalsIgnoreCase("Y") ||choice.equalsIgnoreCase("N")){
					break; 
				} else if (choice.equalsIgnoreCase("Back")) {
					// Returning back
					choice="";
					continue;
				}else{ 
					System.out.println("Error:Invalid choice please enter 'Yes'/'Y' if you want to create a new model or 'No'/'N' if you want to load an existing model");
				} 
			}   

			if(choice.equalsIgnoreCase("No") || choice.equalsIgnoreCase("N")){
				System.out.println("Enter the filename of the RDF model you want to load or 'Back' if you want to return to file selection:");
				String filename=scanner.nextLine();
				if (filename.equalsIgnoreCase("Back")) {
					// Returning back
					continue;
				}
				String currentFileName=filename.endsWith(".rdf")? filename:filename+".rdf";

				// Checking if the file exists
				if(new File(currentFileName).exists()) {
					//Loading the existing model
					RDF_Management.loadModel(currentFileName);
				}else{ 
					System.out.println("Error:The file "+ currentFileName+" does not exist.You need to create a new file");
					// Creating a new model
					RDF_Management.createModel(scanner);
				} 
			}else if(choice.equalsIgnoreCase("Yes") || choice.equalsIgnoreCase("Y")){
				System.out.println("Press enter if you want to continue or enter 'Back' if you want to return to file selection:");
				String filename=scanner.nextLine();
				if (filename.equalsIgnoreCase("Back")) {
					// Returning back
					continue;
				}
				// Creating a new model
				RDF_Management.createModel(scanner);
			}

			// UI Menu 
			String action="";
			while (true){
				System.out.println("Do you want to add or to remove an element?");
				action=scanner.nextLine();

				if(action.equalsIgnoreCase("Add")){
					// Adding a new element
					Legal_Element.addElement(scanner); 
					RDF_Management.saveModel(RDF_Management.currentFileName);

				}else if(action.equalsIgnoreCase("Remove")){
					// Removing an existing element
					Legal_Element.removeElement(scanner);
					RDF_Management.saveModel(RDF_Management.currentFileName);

					// Exiting
				}else if(action.equalsIgnoreCase("Exit")){
					scanner.close();  
					return;

					// Returning
				} else if (action.equalsIgnoreCase("Back")) {
					break;

				}else {
					// Wrong choice
					System.out.println("Error:Invalid choice. Enter 'Add'/'Remove' for element modification or 'Back' if you want to return to file selection\nEnter 'Exit' if you want to quit");
				}
			}  

		}
	} 
}