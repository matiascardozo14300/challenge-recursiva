package models;

public class Persona {
	
	private String nombre;
	private int edad;
	private String equipo;
	private String estadoCivil;
	private String nivelEstudios;
	
	public Persona(String nombre, int edad, String equipo, String estadoCivil, String nivelEstudios) {
		this.nombre = nombre;
		this.edad = edad;
		this.equipo = equipo;
		this.estadoCivil = estadoCivil;
		this.nivelEstudios = nivelEstudios;
	}
	
	public Persona() {
		
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public String getEquipo() {
		return equipo;
	}
	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}
	public String getEstadoCivil() {
		return estadoCivil;
	}
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
	public String getNivelEstudios() {
		return nivelEstudios;
	}
	public void setNivelEstudios(String nivelEstudios) {
		this.nivelEstudios = nivelEstudios;
	}
}
