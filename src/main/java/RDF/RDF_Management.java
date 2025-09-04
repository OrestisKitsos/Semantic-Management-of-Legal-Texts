package RDF;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.model.vocabulary.DC; 
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import java.io.File;
import java.io.FileInputStream; 
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;


public class RDF_Management{
	public static Model model;
	private static SimpleValueFactory factory=SimpleValueFactory.getInstance();
	public static String currentFileName=".rdf";
	public static void createModel(Scanner scanner){

		model=new TreeModel();
		// Creating a new legal resource
		System.out.println("Enter the title for the new RDF Legal Resource:");
		String title=scanner.nextLine();
		System.out.println("Give a name for the new RDF file");
		String newfilename=scanner.nextLine();

		// Creating a new RDF file
		currentFileName=newfilename.endsWith(".rdf")? newfilename:newfilename+".rdf";
		File newFile=new File(currentFileName);

		// Creating the prefixes 
		model.setNamespace("rdfs",RDFS.NAMESPACE);
		model.setNamespace("dc",DC.NAMESPACE);
		model.setNamespace("example","http://example.org/vocab/");

		// Creating the basic classes and relations
		createClassesAndProperties();

		// Adding the new legal resource to the model
		IRI lawIRI=factory.createIRI("http://example.org/vocab/",title.replace(" ","_"));
		model.add(lawIRI,RDF.TYPE,factory.createIRI("http://example.org/vocab/","Legal_Resource"));

		IRI consists_ofIRI = factory.createIRI("http://example.org/vocab/", "consists_of");
		IRI comprisesIRI = factory.createIRI("http://example.org/vocab/", "comprises");
		IRI Legal_ElementIRI=factory.createIRI("http://example.org/vocab/","Legal_Element");
		RDF_Management.model.add(lawIRI, consists_ofIRI, Legal_ElementIRI);
		RDF_Management.model.add(Legal_ElementIRI, comprisesIRI, lawIRI);
		model.add(Legal_ElementIRI,RDF.TYPE,factory.createIRI("http://example.org/vocab/","Legal_Element"));

		saveModel(newFile.getPath());
		System.out.println("The new RDF file was successfully created at:"+newFile.getAbsolutePath());  
	}
	private static void createClassesAndProperties(){

		// Creating the RDF Classes
		IRI ChapterRDFClass=factory.createIRI("http://example.org/vocab/","Chapter");
		IRI ArticleRDFClass=factory.createIRI("http://example.org/vocab/","Article");
		IRI SectionRDFClass=factory.createIRI("http://example.org/vocab/","Section");
		IRI ParagraphRDFClass=factory.createIRI("http://example.org/vocab/","Paragraph");
		IRI IntendRDFClass=factory.createIRI("http://example.org/vocab/","Intend");
		IRI SubintendRDFClass=factory.createIRI("http://example.org/vocab/","Subintend");
		IRI ReferenceRDFClass=factory.createIRI("http://example.org/vocab/","Reference");
		IRI ExceptionRDFClass=factory.createIRI("http://example.org/vocab/","Exception");
		IRI Part_of_SectionRDFClass=factory.createIRI("http://example.org/vocab/","Part_of_Section");
		IRI Legal_ElementRDFClass=factory.createIRI("http://example.org/vocab/","Legal_Element");
		IRI Legal_ResourceRDFClass=factory.createIRI("http://example.org/vocab/","Legal_Resource");
		IRI CharacteristicRDFClass=factory.createIRI("http://example.org/vocab/","Characteristic");

		// Defining the XSD
		IRI XSD_INT = factory.createIRI("http://www.w3.org/2001/XMLSchema#int");
		IRI XSD_STRING = factory.createIRI("http://www.w3.org/2001/XMLSchema#string");

		// Creating the relationships 
		IRI includesIRI=factory.createIRI("http://example.org/vocab/","includes");
		IRI is_included_inIRI=factory.createIRI("http://example.org/vocab/","is_included_in");
		IRI has_numberIRI=factory.createIRI("http://example.org/vocab/","has_number");
		IRI has_letterIRI=factory.createIRI("http://example.org/vocab/","has_letter");
		IRI has_titleIRI=factory.createIRI("http://example.org/vocab/","has_title");
		IRI has_descriptionIRI=factory.createIRI("http://example.org/vocab/","has_description");
		IRI can_modifyIRI=factory.createIRI("http://example.org/vocab/","can_modify");
		IRI can_be_modified_byIRI=factory.createIRI("http://example.org/vocab/","can_be_modified_by");
		IRI consists_ofIRI=factory.createIRI("http://example.org/vocab/","consists_of");
		IRI comprisesIRI=factory.createIRI("http://example.org/vocab/","comprises");
		IRI can_be_anIRI=factory.createIRI("http://example.org/vocab/","can_be_an");
		IRI can_make_aIRI=factory.createIRI("http://example.org/vocab/","can_make_a");
		IRI refers_toIRI=factory.createIRI("http://example.org/vocab/","refers_to");
		IRI is_referred_toIRI=factory.createIRI("http://example.org/vocab/","is_referred_to");
		IRI modifiesIRI=factory.createIRI("http://example.org/vocab/","modifies");
		IRI is_modified_byIRI=factory.createIRI("http://example.org/vocab/","is_modified_by");

		// Creating the properties 

		// Chapter includes a Section
		model.add(includesIRI,RDFS.DOMAIN,ChapterRDFClass);
		model.add(includesIRI,RDFS.RANGE,SectionRDFClass);
		model.add(is_included_inIRI,OWL.INVERSEOF,includesIRI);
		model.add(is_included_inIRI,RDFS.DOMAIN,SectionRDFClass);
		model.add(is_included_inIRI,RDFS.RANGE,ChapterRDFClass);

		// Chapter includes an Article
		model.add(includesIRI,RDFS.DOMAIN,ChapterRDFClass);
		model.add(includesIRI,RDFS.RANGE,ArticleRDFClass);
		model.add(is_included_inIRI,OWL.INVERSEOF,includesIRI);
		model.add(is_included_inIRI,RDFS.DOMAIN,ArticleRDFClass);
		model.add(is_included_inIRI,RDFS.RANGE,ChapterRDFClass);

		// Article includes an Intend
		model.add(includesIRI,RDFS.DOMAIN,ArticleRDFClass);
		model.add(includesIRI,RDFS.RANGE,IntendRDFClass);
		model.add(is_included_inIRI,OWL.INVERSEOF,includesIRI);
		model.add(is_included_inIRI,RDFS.DOMAIN,IntendRDFClass);
		model.add(is_included_inIRI,RDFS.RANGE,ArticleRDFClass);

		// Paragraph includes an Intend                    
		model.add(includesIRI,RDFS.DOMAIN,ParagraphRDFClass);              
		model.add(includesIRI,RDFS.RANGE,IntendRDFClass);
		model.add(is_included_inIRI,OWL.INVERSEOF,includesIRI);
		model.add(is_included_inIRI,RDFS.DOMAIN,IntendRDFClass);
		model.add(is_included_inIRI,RDFS.RANGE,ParagraphRDFClass);

		// Paragraph includes a Subintend
		model.add(includesIRI,RDFS.DOMAIN,ParagraphRDFClass);
		model.add(includesIRI,RDFS.RANGE,SubintendRDFClass);
		model.add(is_included_inIRI,OWL.INVERSEOF,includesIRI);
		model.add(is_included_inIRI,RDFS.DOMAIN,SubintendRDFClass);
		model.add(is_included_inIRI,RDFS.RANGE,ParagraphRDFClass);

		// Chapter has a number
		model.add(has_numberIRI,RDF.TYPE,RDF.PROPERTY);
		model.add(has_numberIRI,RDFS.DOMAIN,ChapterRDFClass);
		model.add(has_numberIRI,RDFS.RANGE,XSD_INT);

		// Article has a number
		model.add(has_numberIRI,RDF.TYPE,RDF.PROPERTY);
		model.add(has_numberIRI,RDFS.DOMAIN,ArticleRDFClass);
		model.add(has_numberIRI,RDFS.RANGE,XSD_INT);

		// Section has a number
		model.add(has_numberIRI,RDF.TYPE,RDF.PROPERTY);
		model.add(has_numberIRI,RDFS.DOMAIN,SectionRDFClass);
		model.add(has_numberIRI,RDFS.RANGE,XSD_INT);

		// Paragraph has a number
		model.add(has_numberIRI,RDF.TYPE,RDF.PROPERTY);
		model.add(has_numberIRI,RDFS.DOMAIN,ParagraphRDFClass);
		model.add(has_numberIRI,RDFS.RANGE,XSD_INT);

		// Intend has a number
		model.add(has_numberIRI,RDF.TYPE,RDF.PROPERTY);
		model.add(has_numberIRI,RDFS.DOMAIN,IntendRDFClass);
		model.add(has_numberIRI,RDFS.RANGE,XSD_INT);

		// Intend has a letter 
		model.add(has_letterIRI,RDF.TYPE,RDF.PROPERTY);
		model.add(has_letterIRI,RDFS.DOMAIN,IntendRDFClass);
		model.add(has_letterIRI,RDFS.RANGE,XSD_STRING);

		// Subintend has a letter
		model.add(has_letterIRI,RDF.TYPE,RDF.PROPERTY);
		model.add(has_letterIRI,RDFS.DOMAIN,SubintendRDFClass);
		model.add(has_letterIRI,RDFS.RANGE,XSD_STRING);

		// Chapter has a title
		model.add(has_titleIRI,RDF.TYPE,RDF.PROPERTY);
		model.add(has_titleIRI,RDFS.DOMAIN,ChapterRDFClass);
		model.add(has_titleIRI,RDFS.RANGE,XSD_STRING);

		// Article has a title 
		model.add(has_titleIRI,RDF.TYPE,RDF.PROPERTY);
		model.add(has_titleIRI,RDFS.DOMAIN,ArticleRDFClass);
		model.add(has_titleIRI,RDFS.RANGE,XSD_STRING);

		// Section has a title
		model.add(has_titleIRI,RDF.TYPE,RDF.PROPERTY);
		model.add(has_titleIRI,RDFS.DOMAIN,SectionRDFClass);
		model.add(has_titleIRI,RDFS.RANGE,XSD_STRING);

		// Article can modify an Article
		model.add(can_modifyIRI,RDFS.DOMAIN,ArticleRDFClass);
		model.add(can_modifyIRI,RDFS.RANGE,ArticleRDFClass);
		model.add(can_be_modified_byIRI,OWL.INVERSEOF,can_modifyIRI);
		model.add(can_be_modified_byIRI,RDFS.DOMAIN,ArticleRDFClass);
		model.add(can_be_modified_byIRI,RDFS.RANGE,ArticleRDFClass);

		// Article has a description
		model.add(has_descriptionIRI,RDF.TYPE,RDF.PROPERTY);
		model.add(has_descriptionIRI,RDFS.DOMAIN,ArticleRDFClass);
		model.add(has_descriptionIRI,RDFS.RANGE,XSD_STRING);

		// Paragraph has a description 
		model.add(has_descriptionIRI,RDF.TYPE,RDF.PROPERTY);
		model.add(has_descriptionIRI,RDFS.DOMAIN,ParagraphRDFClass);
		model.add(has_descriptionIRI,RDFS.RANGE,XSD_STRING);

		// Intend can be an Exception 
		model.add(can_be_anIRI,RDFS.DOMAIN,IntendRDFClass);
		model.add(can_be_anIRI,RDFS.RANGE,ExceptionRDFClass);

		// Subintend can be an Exception
		model.add(can_be_anIRI,RDFS.DOMAIN,SubintendRDFClass);
		model.add(can_be_anIRI,RDFS.RANGE,ExceptionRDFClass);

		// Intend can make a Reference
		model.add(can_make_aIRI,RDFS.DOMAIN,IntendRDFClass); 
		model.add(can_make_aIRI,RDFS.RANGE,ReferenceRDFClass);

		// Subintend can make a Reference
		model.add(can_make_aIRI,RDFS.DOMAIN,SubintendRDFClass);
		model.add(can_make_aIRI,RDFS.RANGE,ReferenceRDFClass);

		// Reference refers to Paragraph
		model.add(refers_toIRI,RDFS.DOMAIN,ReferenceRDFClass);
		model.add(refers_toIRI,RDFS.RANGE,ParagraphRDFClass);
		model.add(is_referred_toIRI,OWL.INVERSEOF,refers_toIRI);
		model.add(is_referred_toIRI,RDFS.DOMAIN,ParagraphRDFClass);
		model.add(is_referred_toIRI,RDFS.RANGE,ReferenceRDFClass);

		// Reference refers to Article
		model.add(refers_toIRI,RDFS.DOMAIN,ReferenceRDFClass);
		model.add(refers_toIRI,RDFS.RANGE,ArticleRDFClass);
		model.add(is_referred_toIRI,OWL.INVERSEOF,refers_toIRI);
		model.add(is_referred_toIRI,RDFS.DOMAIN,ArticleRDFClass);
		model.add(is_referred_toIRI,RDFS.RANGE,ReferenceRDFClass);

		// Exception modifies Paragraph
		model.add(modifiesIRI,RDFS.DOMAIN,ExceptionRDFClass);
		model.add(modifiesIRI,RDFS.RANGE,ParagraphRDFClass);
		model.add(is_modified_byIRI,OWL.INVERSEOF,modifiesIRI);
		model.add(is_modified_byIRI,RDFS.DOMAIN,ParagraphRDFClass);
		model.add(is_modified_byIRI,RDFS.RANGE,ExceptionRDFClass);

		// Section consists of Part_of_Section
		model.add(consists_ofIRI,RDFS.DOMAIN,SectionRDFClass);
		model.add(consists_ofIRI,RDFS.RANGE,Part_of_SectionRDFClass);
		model.add(comprisesIRI,OWL.INVERSEOF,consists_ofIRI);
		model.add(comprisesIRI,RDFS.DOMAIN,Part_of_SectionRDFClass);
		model.add(comprisesIRI,RDFS.RANGE,SectionRDFClass);

		// Legal_Resource consists of Legal_Element 
		model.add(consists_ofIRI,RDFS.DOMAIN,Legal_ResourceRDFClass);
		model.add(consists_ofIRI,RDFS.RANGE,Legal_ElementRDFClass);
		model.add(comprisesIRI,OWL.INVERSEOF,consists_ofIRI);
		model.add(comprisesIRI,RDFS.DOMAIN,Legal_ResourceRDFClass);
		model.add(comprisesIRI,RDFS.RANGE,Legal_ElementRDFClass);

		// Legal_Element is a subclass of Legal_Resource
		model.add(Legal_ElementRDFClass,RDFS.SUBCLASSOF,Legal_ResourceRDFClass);

		// Chapter is a Legal_Element
		model.add(ChapterRDFClass,RDFS.SUBCLASSOF,Legal_ElementRDFClass);

		// Article is a Legal_Element
		model.add(ArticleRDFClass,RDFS.SUBCLASSOF,Legal_ElementRDFClass);

		// Section is a Legal_Element
		model.add(SectionRDFClass,RDFS.SUBCLASSOF,Legal_ElementRDFClass);

		// Intend is a Legal_Element 
		model.add(IntendRDFClass,RDFS.SUBCLASSOF,Legal_ElementRDFClass);

		// Subintend is a Legal_Element  
		model.add(SubintendRDFClass,RDFS.SUBCLASSOF,Legal_ElementRDFClass);

		// Paragraph is a Legal_Element
		model.add(ParagraphRDFClass,RDFS.SUBCLASSOF,Legal_ElementRDFClass);

		// Part_of_section is a subclass of Section
		model.add(Part_of_SectionRDFClass,RDFS.SUBCLASSOF,SectionRDFClass);

		// Exception is a Characteristic 
		model.add(ExceptionRDFClass,RDFS.SUBCLASSOF,CharacteristicRDFClass);

		// Reference is a Characteristic 
		model.add(ReferenceRDFClass,RDFS.SUBCLASSOF,CharacteristicRDFClass);

	}

	public static void loadModel(String filename){
		File existingFile=new File(filename);
		if (existingFile.exists()){
			try(InputStream input=new FileInputStream(existingFile)){
				model=Rio.parse(input,"",RDFFormat.TURTLE);
				System.out.println("Loading the existing model from the export file");
				model.forEach(statement->{
					System.out.println(statement.getPredicate()+""+
							statement.getSubject()+""+
							statement.getObject());
				});

				// Saving the changes made on an existing model
				currentFileName = filename;
			}catch(Exception ex) { 
				ex.printStackTrace();
			}
		}else{
			System.out.println("Error:The file does not exist.You need to create a new file:");
		}  
	}

	public static void saveModel(String filename){ 
		try(OutputStream out=new FileOutputStream(new File(filename))){
			Rio.write(model,out,RDFFormat.TURTLE);
			System.out.println("The model was successfully saved to:"+filename);
			model.forEach(statement->{
				System.out.println(statement.getPredicate()+""+
						statement.getSubject()+""+
						statement.getObject()); 
			}); 
		}catch(Exception ex){  
			ex.printStackTrace(); 
		} 
	}  
} 