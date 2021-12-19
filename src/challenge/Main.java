package challenge;

import java.io.*;
import models.Persona;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
public class Main {

	public static void main(String[] args) throws Exception {
		
		String line = "";
		String splitBy = ";";
		
		try {
			
			Scanner entrada = new Scanner(System.in);
			System.out.println("Ingrese la ruta del archivo '.csv': ");
			String ruta = entrada.nextLine();
			entrada.close();
			
			BufferedReader br = new BufferedReader(new FileReader(ruta));
			
			List<Persona> personas = new ArrayList<Persona>();
			Set<String> equipos = new HashSet<String>();
						
			
			while ((line = br.readLine()) != null) {
				String[] data = line.split(splitBy);
				
				Persona p = new Persona(data[0], Integer.parseInt(data[1]), data[2], data[3], data[4]);
				
				equipos.add(p.getEquipo());
				
				personas.add(p);
			}
			
			puntoUno(personas);
			
			puntoDos(personas);
			
			puntoTres(personas);
			
			puntoCuatro(personas);
			
			puntoCinco(personas, equipos);
			
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * Devuelve por consola la cantidad total de personas registradas.
	 * 
	 * @param personas una Lista de objetos de tipo Persona(String nombre, int edad, String equipo, String estadoCivil, String nivelEstudios)
	 */
	private static void puntoUno(List<Persona> personas) {
		System.out.println();
		System.out.println("1. La cantidad total de personas registradas es de: " + personas.size());
	}
	
	
	/**
	 * Devuelve por consola el promedio de edad de los socios de Racing.
	 * 
	 * @param personas una Lista de objetos de tipo Persona(String nombre, int edad, String equipo, String estadoCivil, String nivelEstudios)
	 */
	private static void puntoDos(List<Persona> personas) {
		List<Persona> racing = personas.stream()
				.filter(persona -> persona.getEquipo().equalsIgnoreCase("Racing"))
				.collect(Collectors.toList());
		
		double sumEdades = 0;
		
		for(Persona p : racing) {
			sumEdades = sumEdades + p.getEdad();
		}
		
		System.out.println();
		System.out.println("2. El promedio de edad de los socios de Racing es de: " + String.format("%.2f",sumEdades/racing.size()) + " años");
	}
	
	
	/**
	 * Devuelve por consola las primeras 100 personas casadas, con estudios Universitarios, ordenadas por edad.
	 * 
	 * @param personas una Lista de objetos de tipo Persona(String nombre, int edad, String equipo, String estadoCivil, String nivelEstudios)
	 */
	private static void puntoTres(List<Persona> personas) {
		List<Persona> casadosUni = personas.stream()
				.filter(persona -> persona.getEstadoCivil().equalsIgnoreCase("Casado"))
				.filter(persona -> persona.getNivelEstudios().equalsIgnoreCase("Universitario"))
				.collect(Collectors.toList());
		
		List<Persona> casadosUniCien = casadosUni.stream().limit(100).collect(Collectors.toList());
		
		List<Persona> casadosUniOrdenados = casadosUniCien.stream()
				.sorted(Comparator.comparing(Persona::getEdad))
				.collect(Collectors.toList());
		
		int countCien = 0;
		
		System.out.println();
		System.out.println("3. Las primeras 100 personas casadas, con estudios Universitarios, ordenadas por edad son: ");
		System.out.printf("    %-6s%-12s%-12s %-12s\n", "N°", "Nombre", "Edad", "Equipo");
		
		for(Persona p : casadosUniOrdenados) {
			countCien ++;
			System.out.printf("    %-6s%-12s %-12d%-6s\n", countCien + "° ", p.getNombre(), p.getEdad(), p.getEquipo());
		}
	}
	
	
	/**
	 * Devuelve por consola los 5 nombres más comunes entre los hichas de River.
	 * 
	 * @param personas una Lista de objetos de tipo Persona(String nombre, int edad, String equipo, String estadoCivil, String nivelEstudios)
	 */
	private static void puntoCuatro(List<Persona> personas) {
		Map<String, List<Persona>> groupByNombre = personas.stream()
				.filter(persona -> persona.getEquipo().equalsIgnoreCase("River"))
				.collect(Collectors.groupingBy(Persona::getNombre));
		
		Map<String, Integer> hashRiver = new HashMap<String, Integer>();
		List<Integer> listaRiver = new ArrayList<Integer>();
		
		groupByNombre.forEach((nombre, personas1) -> {
			hashRiver.put(nombre, personas1.size());
		});
		
		for (String i : hashRiver.keySet()) {
			listaRiver.add(hashRiver.get(i));
		}
		
		Set<Integer> setRiver = new HashSet<>(listaRiver);
		List<Integer> listaRiverOrdenada = new ArrayList<>(setRiver);
		Collections.sort(listaRiverOrdenada);
		Collections.reverse(listaRiverOrdenada);
		
		System.out.println();
		System.out.println("4. Los 5 nombres más comunes entre los hinchas de River son: ");
		System.out.printf("    %-6s%-12s%6s\n", "N°", "Nombre", "Cantidad");
		
		int countCinco = 0;
		
		for (int value:listaRiverOrdenada) {
			for (String key : getKeys(hashRiver, value)) {
				if(countCinco<5) {
					countCinco ++;
					System.out.printf("    %-6s%-12s%6d\n",countCinco + "° ", key, value);
				}else {
					break;
				}
			}
		}
	}
	
	
	/**
	 * Devuelve por consola un listado de equipos, ordenado de mayor a menor según la cantidad de socios, 
	 * con el promedio de edad de sus socios, la menor edad registrada y la mayor edad registrada.
	 * 
	 * @param personas una Lista de objetos de tipo Persona(String nombre, int edad, String equipo, String estadoCivil, String nivelEstudios)
	 * @param equipos un Set de Strings que tiene el nombre de todos los equipos.
	 */
	private static void puntoCinco(List<Persona> personas, Set<String> equipos) {
		Map<String, List<Double>> hashEdades = new HashMap<String, List<Double>>();
		
		for (String e : equipos) {
			List<Double> listaEdades = new ArrayList<Double>();
			
			Double promedio = personas.stream()
					.filter(persona -> persona.getEquipo().equalsIgnoreCase(e))
					.mapToDouble(Persona::getEdad)
					.average()
					.orElse(Double.NaN);
				
				listaEdades.add(promedio);
				
				personas.stream()
					.filter(persona -> persona.getEquipo().equalsIgnoreCase(e))
					.min(Comparator.comparing(Persona::getEdad))
					.ifPresent(persona -> {
						listaEdades.add(Double.valueOf(persona.getEdad()));
					});
				
				personas.stream()
					.filter(persona -> persona.getEquipo().equalsIgnoreCase(e))
					.max(Comparator.comparing(Persona::getEdad))
					.ifPresent(persona -> {
						listaEdades.add(Double.valueOf(persona.getEdad()));
					});
				
				hashEdades.put(e, listaEdades);
		}
		
		System.out.println();
		System.out.println("5. Listado de equipos, ordenado de mayor a menor según la cantidad de socios, con el promedio de edad de sus socios, la menor edad registrada y la mayor edad registrada: ");
		
		Map<String, List<Persona>> groupByEquipo = personas.stream()
				.collect(Collectors.groupingBy(Persona::getEquipo));
		
		Map<String, Integer> hashEquipos = new HashMap<String, Integer>();
		List<Integer> listaEquipos = new ArrayList<Integer>();
		
		groupByEquipo.forEach((equipo, personas2) -> {
			hashEquipos.put(equipo, personas2.size());
		});
		
		for (String i : hashEquipos.keySet()) {
			listaEquipos.add(hashEquipos.get(i));
		}
		
		Set<Integer> setPuntoCinco = new HashSet<>(listaEquipos);
		List<Integer> listaEquiposDos = new ArrayList<>(setPuntoCinco);
		Collections.sort(listaEquiposDos);
		Collections.reverse(listaEquiposDos);
		
		System.out.printf("    %-20s%-20s%-20s %6s\n", "Equipo", "Promedio Edad", "Menor Edad", "Mayor Edad");
		for (int value:listaEquiposDos) {
			for (String key : getKeys(hashEquipos, value)) {
				System.out.printf("    %-20s     %-19.2f%-20.0f%3.0f\n", key, hashEdades.get(key).get(0), hashEdades.get(key).get(1), hashEdades.get(key).get(2));
			}
		}
	}
	
	
	/**
	 * Devuelve un Set de Strings con las claves de un HashMap que tienen como valor a un int determinado.
	 * 
	 * @param map Map de clave String y valor Integer dónde quiro buscar.
	 * @param value valor del cual quiero obtener las claves.
	 * @return result Set con todas las claves que tienen como valor al @param value.
	 */
	private static Set<String> getKeys(Map<String, Integer> map, Integer value) {

	      Set<String> result = new HashSet<>();
	      if (map.containsValue(value)) {
	          for (Map.Entry<String, Integer> entry : map.entrySet()) {
	              if (Objects.equals(entry.getValue(), value)) {
	                  result.add(entry.getKey());
	              }
	          }
	      }
	      return result;

	  }

}
