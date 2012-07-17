package fr.cnrs.liris.tagomatic.tag;

public class GenericLocationTagClass implements SemanticTagClass {
	
	private String name ;
	private final String  id  = "Generic_Location" ;;

	public GenericLocationTagClass() {
		// TODO Auto-generated constructor stub
	}
	
	
	public GenericLocationTagClass(String name) {
		super();
		this.name = name;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getId() {
		return id;
	}
	
	
	

}
