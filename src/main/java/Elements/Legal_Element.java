package Elements;

import org.eclipse.rdf4j.model.IRI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import java.util.Arrays;
import RDF.RDF_Management;
import java.util.UUID;
public abstract class Legal_Element{
	protected String name;
	protected String title;
	protected String letter;
	protected int number;
	protected String description;
	protected String exception;
	protected String reference;
	public List<Legal_Element> elements;

	public Legal_Element(String name,String title,String letter,int number,String description,String exception,String reference) {
		this.name=name;
		this.title=title;
		this.letter=letter;
		this.number=number;
		this.description=description;
		this.exception=exception;
		this.reference=reference;
		this.elements=new ArrayList<>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name=name;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title) {
		this.title=title;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter=letter;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number=number;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description=description;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception=exception;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference=reference;
	}


	private static String generateRandomName() {
		// Generate a random UUID and return only the first 10 characters
		return UUID.randomUUID().toString().substring(0,10);
	}

	private static SimpleValueFactory factory=SimpleValueFactory.getInstance();
	public static void addElement(Scanner scanner) {
		while (true) {
			System.out.println("Enter the class of the element you want to add (Chapter, Article, Section, Intend, Subintend, Paragraph) or 'Back' to return to the menu:");
			String className = scanner.nextLine();
			// Returning back
			if (className.equalsIgnoreCase("Back")) {
				break;
			}

			// Checking if the class given by user is valid
			if (!isValidLegalClass(className)) {
				System.out.println("Error:Invalid class.Please enter a valid class.");
				continue;
			}

			// Creating a random string for instance name 
			String instanceName = generateRandomName();
			System.out.println("Generated unique instance name: " + instanceName);

			// Creating a new instance for the class
			IRI classIRI = factory.createIRI("http://example.org/vocab/", className);
			IRI instanceIRI = factory.createIRI("http://example.org/vocab/", instanceName);

			// Adding the new instance to the RDF model
			RDF_Management.model.add(instanceIRI, RDF.TYPE, classIRI);

			// Adding relationship with another instance of a Legal Element class
			askForLegalElementCLass(scanner, instanceIRI,className);

			// Adding object properties for the instance
			addClassProperties(scanner, className, instanceIRI);

			System.out.println("Element of class " + className + " with instance " + instanceName + " added successfully.");
			// Saving to model
			RDF_Management.saveModel(RDF_Management.currentFileName);
		}
	}

	private static boolean isValidLegalClass(String className) {
		return Arrays.asList("Chapter", "Article", "Section", "Intend", "Subintend", "Paragraph").contains(className);
	}

	private static void askForLegalElementCLass(Scanner scanner, IRI instanceIRI,String childClass) {
		System.out.println("Do you want to attach this to another Legal Element class? (Yes/No)");
		String response = scanner.nextLine();
		if (response.equalsIgnoreCase("Yes") || response.equalsIgnoreCase("Y")){
			String LegalElementClass;
			while (true) {
				System.out.println("Enter the Legal Element class (Chapter, Article, Section, Intend, Subintend, Paragraph):");
				LegalElementClass = scanner.nextLine();
				if (isValidLegalClass(LegalElementClass)) {
					break;
				}
				System.out.println("Error: Invalid class. Please enter a valid class.");
			}

			IRI elementIRI = factory.createIRI("http://example.org/vocab/", LegalElementClass);
			List<String> elementInstances = getInstances(elementIRI);

			// Checking if the Legal Element class has no instances
			if (elementInstances.isEmpty()) {
				System.out.println("No instances found for class " + LegalElementClass);
				System.out.println("Would you like to create a new instance for this class? (Yes/No)");
				String createInstanceResponse = scanner.nextLine();
				if (createInstanceResponse.equalsIgnoreCase("Yes") || response.equalsIgnoreCase("Y")){
					String newInstanceName = generateRandomName();
					System.out.println("Generated unique instance name: " + newInstanceName);
					IRI newInstanceIRI = factory.createIRI("http://example.org/vocab/", newInstanceName);
					RDF_Management.model.add(newInstanceIRI, RDF.TYPE, elementIRI);
					addClassProperties(scanner, LegalElementClass, newInstanceIRI);

					// Updating the list of instances
					elementInstances.add(newInstanceName);
				} else {
					return;
				}
			}  

			System.out.println("Available instances: " + elementInstances);
			System.out.println("Enter the child instance name:");
			String childInstanceName = scanner.nextLine();

			// Checking if the Legal Element instance is valid
			if (!elementInstances.contains(childInstanceName)) {
				System.out.println("Error: Instance not found.");
				return;
			}

			// Check if the connection is valid based on the defined rules
			if (!isValidConnection(LegalElementClass,childClass)) {
				System.out.println("Error: Invalid connection between classes.");
				return;
			}

			IRI parentInstanceIRI = factory.createIRI("http://example.org/vocab/", childInstanceName);
			IRI includesIRI = factory.createIRI("http://example.org/vocab/", "includes");
			IRI is_included_inIRI = factory.createIRI("http://example.org/vocab/", "is_included_in");

			// Adding the relationship to the model
			RDF_Management.model.add(instanceIRI, includesIRI,parentInstanceIRI);
			RDF_Management.model.add(parentInstanceIRI, is_included_inIRI,instanceIRI);
		}
	} 


	private static void addClassProperties(Scanner scanner, String className, IRI instanceIRI) {
		switch (className) {

		case "Chapter":
			addInstanceProperty(scanner, instanceIRI, "has_number", "Number");
			addInstanceProperty(scanner, instanceIRI, "has_title", "Title");
			break;

		case "Article":
			addInstanceProperty(scanner, instanceIRI, "has_number", "Number");
			addInstanceProperty(scanner, instanceIRI, "has_title", "Title");
			addInstanceProperty(scanner, instanceIRI, "has_description", "Description");
			askIfModifier(scanner, instanceIRI);
			askToAddReference(scanner, instanceIRI);
			break;

		case "Section":
			addInstanceProperty(scanner, instanceIRI, "has_number", "Number");
			addInstanceProperty(scanner, instanceIRI, "has_title", "Title");
			addInstanceProperty(scanner, instanceIRI, "consists_of", "Part_of_Section");
			break;

		case "Paragraph":
			addInstanceProperty(scanner, instanceIRI, "has_number", "Number");
			addInstanceProperty(scanner, instanceIRI, "has_description", "Description");
			break;

		case "Intend":
			addInstanceProperty(scanner, instanceIRI, "has_number", "Number");
			addInstanceProperty(scanner, instanceIRI, "has_letter", "Letter");;
			askIfException(scanner, instanceIRI);
			askToAddReference(scanner, instanceIRI);
			break;

		case "Subintend":
			addInstanceProperty(scanner, instanceIRI, "has_letter", "Letter");
			askIfException(scanner, instanceIRI);
			askToAddReference(scanner, instanceIRI);
			break;

		default:
			System.out.println("No additional properties for class " + className);
		}
	}

	private static boolean isValidConnection(String childClass, String parentClass) {
		System.out.println("Validating connection: ParentClass = " + parentClass + ", ChildClass = " + childClass);

		// Chapter includes Section or Article
		if (parentClass.equals("Chapter") && (childClass.equals("Section") || childClass.equals("Article"))) {
			return true;
		}
		// Article includes Intend or Paragraph
		if (parentClass.equals("Article") && (childClass.equals("Intend") || childClass.equals("Paragraph"))) {
			return true;
		}
		// Paragraph includes Intend or Subintend
		if (parentClass.equals("Paragraph") && (childClass.equals("Intend") || childClass.equals("Subintend"))) {
			return true;
		}
		// Default case for invalid connections
		return false; 
	} 

	private static void addInstanceProperty(Scanner scanner, IRI instanceIRI, String propertyName, String rangeName) {
		System.out.println("Enter the " + rangeName + " for this instance:");
		String value = scanner.nextLine();

		IRI propertyIRI = factory.createIRI("http://example.org/vocab/", propertyName);

		// If rangeName equals "Number", save the value as a Literal without quotes and with the correct datatype
		if (rangeName.equals("Number") || rangeName.equals("Letter") || rangeName.equals("Title") || rangeName.equals("Description")){

			// Handle other range types like "Letter", "Title", etc. as strings
			org.eclipse.rdf4j.model.Literal literalValue = factory.createLiteral(value);
			RDF_Management.model.add(instanceIRI, propertyIRI, literalValue);

		} else { 

			// Repeat for other range names if needed
			String valueInstanceName = rangeName + "_" + value.replaceAll("\\s+", "_");
			IRI valueInstanceIRI = factory.createIRI("http://example.org/vocab/", valueInstanceName);

			// Creating the instance and connect
			RDF_Management.model.add(valueInstanceIRI, RDF.TYPE, factory.createIRI("http://example.org/vocab/", rangeName));
			RDF_Management.model.add(instanceIRI, propertyIRI, valueInstanceIRI);

			// Adding inverse property if it exists
			IRI inversePropertyIRI = getInversePropertyIRI(propertyIRI);
			if (inversePropertyIRI != null) {
				RDF_Management.model.add(valueInstanceIRI, inversePropertyIRI, instanceIRI);
			}

		}
	}



	private static IRI getInversePropertyIRI(IRI propertyIRI) {
		// Defining the inverse properties for known relations
		if (propertyIRI.getLocalName().equals("refers_to")) {
			return factory.createIRI("http://example.org/vocab/", "is_referred_to");
		}else if(propertyIRI.getLocalName().equals("consists_of")) {
			return factory.createIRI("http://example.org/vocab/", "comprises");
		}
		return null;
	}


	private static void askIfModifier(Scanner scanner,IRI instanceIRI){
		System.out.println("Is this article a modifer? (Yes/No)");
		String response = scanner.nextLine();
		if (response.equalsIgnoreCase("Yes") || response.equalsIgnoreCase("Y")){

			// Get all instances of the class Paragraph
			IRI articleIRI = factory.createIRI("http://example.org/vocab/", "Article");
			List<String> articleInstances = getInstances(articleIRI);

			if (articleInstances.isEmpty()) {
				System.out.println("Error:No instances found for class Article.Exception cannot be created.");
				return;
			}

			// Display available Paragraph instances
			System.out.println("Available Article instances: " + articleInstances);
			System.out.println("Select the Article for modifying:");
			String selectedArticle = scanner.nextLine();

			// Check if the selected instance is valid
			if (!articleInstances.contains(selectedArticle)) {
				System.out.println("Error: Instance not found.The modifier cannot be created.");
				return;
			}

			IRI modifiesIRI = factory.createIRI("http://example.org/vocab/", "modifies");
			IRI is_modified_byIRI = factory.createIRI("http://example.org/vocab/", "is_modified_by");
			IRI selectedParagraphIRI = factory.createIRI("http://example.org/vocab/", selectedArticle);
			RDF_Management.model.add(instanceIRI,modifiesIRI,selectedParagraphIRI);
			RDF_Management.model.add(selectedParagraphIRI,is_modified_byIRI,instanceIRI);

		}
	}
	private static void askIfException(Scanner scanner, IRI instanceIRI) {
		System.out.println("Is this instance an exception? (Yes/No)");
		String response = scanner.nextLine();
		if (response.equalsIgnoreCase("Yes") || response.equalsIgnoreCase("Y")){               

			// Get all instances of the class Paragraph
			IRI paragraphIRI = factory.createIRI("http://example.org/vocab/", "Paragraph");
			List<String> paragraphInstances = getInstances(paragraphIRI);

			if (paragraphInstances.isEmpty()) {
				System.out.println("No instances found for class Paragraph. Exception cannot be created.");
				return;
			}

			// Display available Paragraph instances
			System.out.println("Available Paragraph instances: " + paragraphInstances);
			System.out.println("Select the Paragraph for modifying:");
			String selectedParagraph = scanner.nextLine();

			// Check if the selected instance is valid
			if (!paragraphInstances.contains(selectedParagraph)) {
				System.out.println("Error: Instance not found. Exception cannot be created.");
				return;
			}


			// Create the Exception instance

			// Assuming the Exception has a name based on the paragraph
			IRI exceptionIRI = factory.createIRI("http://example.org/vocab/", "Exception_of_" + selectedParagraph); 
			RDF_Management.model.add(exceptionIRI, RDF.TYPE, factory.createIRI("http://example.org/vocab/", "Exception"));

			// Link the Exception to the selected Paragraph
			IRI modifiesIRI = factory.createIRI("http://example.org/vocab/", "modifies");
			IRI isModifiedByIRI = factory.createIRI("http://example.org/vocab/", "is_modified_by");
			IRI selectedParagraphIRI = factory.createIRI("http://example.org/vocab/", selectedParagraph);
			IRI can_be_anIRI=factory.createIRI("http://example.org/vocab/","can_be_an");

			// Update the RDF model to reflect that the Exception modifies the Paragraph
			RDF_Management.model.add(exceptionIRI, modifiesIRI, selectedParagraphIRI);
			RDF_Management.model.add(selectedParagraphIRI, isModifiedByIRI, exceptionIRI);
			RDF_Management.model.add(instanceIRI,can_be_anIRI,exceptionIRI);

			System.out.println("Exception successfully linked to the paragraph!");
		}
	}


	private static void askToAddReference(Scanner scanner, IRI instanceIRI) {
		System.out.println("Do you want to add a reference? (Yes/No)");
		String response = scanner.nextLine();
		if (response.equalsIgnoreCase("Yes") || response.equalsIgnoreCase("Y")){   

			// Create the Reference instance
			// Ask for the class of the target instance (Article or Paragraph)
			System.out.println("Enter the Legal Element class you want to reference (Article, Paragraph):");
			String referenceTargetClass = scanner.nextLine();

			while (!referenceTargetClass.equalsIgnoreCase("Article") && 
					!referenceTargetClass.equalsIgnoreCase("Paragraph")) {
				System.out.println("Error: You can only reference 'Article' or 'Paragraph'. Please enter a valid class.");
				referenceTargetClass = scanner.nextLine();
			}


			IRI referenceTargetClassIRI = factory.createIRI("http://example.org/vocab/", referenceTargetClass);
			List<String> referenceInstances = getInstances(referenceTargetClassIRI);

			if (referenceInstances.isEmpty()) {
				System.out.println("No instances found for class " + referenceTargetClass);
				return;
			}

			System.out.println("Available instances: " + referenceInstances);
			System.out.println("Enter the instance name to reference:");
			String referenceInstanceName = scanner.nextLine();

			if (!referenceInstances.contains(referenceInstanceName)) {
				System.out.println("Error: Instance not found.");
				return;
			} 

			IRI referenceIRI = factory.createIRI("http://example.org/vocab/", "Reference_of_" + referenceInstanceName); 
			RDF_Management.model.add(referenceIRI, RDF.TYPE, factory.createIRI("http://example.org/vocab/", "Reference"));
			IRI referenceInstanceIRI = factory.createIRI("http://example.org/vocab/", referenceInstanceName);

			// Link the Reference to the target instance (Article/Paragraph)
			IRI refersToIRI = factory.createIRI("http://example.org/vocab/", "refers_to");
			IRI isReferredToIRI = factory.createIRI("http://example.org/vocab/", "is_referred_to");
			IRI can_make_aIRI=factory.createIRI("http://example.org/vocab/","can_make_a");
			RDF_Management.model.add(referenceIRI, refersToIRI, referenceInstanceIRI);
			RDF_Management.model.add(referenceInstanceIRI, isReferredToIRI, referenceIRI);
			RDF_Management.model.add(instanceIRI, can_make_aIRI, referenceIRI);

			System.out.println("Reference successfully linked to the selected Legal Element");
		}
	}



	public static void removeElement(Scanner scanner) {
		while (true) {
			System.out.println("Enter the class of the element you want to remove (Chapter, Article, Section, Intend, Subintend, Paragraph) or 'Back' to return to the menu:");
			String className = scanner.nextLine();
			// Returning back
			if (className.equalsIgnoreCase("Back")) {
				break;
			}  

			if (!isValidLegalClass(className)) {
				System.out.println("Error:Invalid class.Please enter a valid class.");
				continue;
			}

			IRI classIRI = factory.createIRI("http://example.org/vocab/", className);
			List<String> instances = getInstances(classIRI);

			// Checking if the class has no instances
			if (instances.isEmpty()) {
				System.out.println("No instances found for class " + className);
				continue;
			}

			System.out.println("Existing instances: " + instances);
			System.out.println("Enter the instance name you want to remove:");
			String instanceName = scanner.nextLine();

			// Checking if the instance given by user is valid
			if (!instances.contains(instanceName)) {
				System.out.println("Error: Instance not found.");
				continue;
			}

			// Removing the existing instance and all its object properties
			IRI instanceIRI = factory.createIRI("http://example.org/vocab/", instanceName);

			// Finding the relationships of the instance
			List<String> connectedRelations = new ArrayList<>();
			RDF_Management.model.filter(instanceIRI, null, null).forEach(statement -> {
				connectedRelations.add("Relation: " + statement.getPredicate().stringValue() + " -> " + statement.getObject().stringValue());
			});

			RDF_Management.model.filter(null, null, instanceIRI).forEach(statement -> {
				connectedRelations.add("Relation: " + statement.getSubject().stringValue() + " -> " + statement.getPredicate().stringValue());
			});

			// Showing the relations
			if (!connectedRelations.isEmpty()) {
				System.out.println("This instance is connected to the following relations:");
				connectedRelations.forEach(System.out::println);

				System.out.println("Are you sure you want to delete this instance? (Yes/No)");
				String confirmation = scanner.nextLine();
				if (!confirmation.equalsIgnoreCase("Yes") && !confirmation.equalsIgnoreCase("Y")){
					continue; 
				}  
			}   
			RDF_Management.model.removeIf(statement -> 
			statement.getSubject().equals(instanceIRI) || 
			statement.getObject().equals(instanceIRI)
					);
			System.out.println("Instance " + instanceName + " removed successfully.");

			// Saving to model
			RDF_Management.saveModel(RDF_Management.currentFileName);
		}
	}

	private static List<String> getInstances(IRI classIRI) {
		List<String> instances = new ArrayList<>();
		RDF_Management.model.filter(null, RDF.TYPE, classIRI).forEach(statement -> {
			instances.add(statement.getSubject().stringValue().replace("http://example.org/vocab/", ""));
		});  
		return instances;
	} 
 
}
