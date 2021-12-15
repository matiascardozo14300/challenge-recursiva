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
		double sumEdades = 0;
		int countCien = 0;
		int countCinco = 0;
		
		try {
			
			Scanner entrada = new Scanner(System.in);
			System.out.println("Ingrese la ruta del archivo '.csv': ");
			String ruta = entrada.nextLine();
			entrada.close();
			
			BufferedReader br = new BufferedReader(new FileReader(ruta));
			
			//HashMap
			Map<String, Integer> hashRiver = new HashMap<String, Integer>();
			Map<String, Integer> hashClubes = new HashMap<String, Integer>();
			
			//Array
			List<Persona> personas = new ArrayList<Persona>();
			List<Integer> listaPuntoCuatro = new ArrayList<Integer>();
			List<Double> listaPuntoCinco = new ArrayList<Double>();
			
			//Set
			Set<String> clubes = new HashSet<String>();
						
			
			while ((line = br.readLine()) != null) {
				String[] data = line.split(splitBy);
				
				Persona p = new Persona(data[0], Integer.parseInt(data[1]), data[2], data[3], data[4]);
				
				clubes.add(p.getEquipo());
				
				personas.add(p);
			}
			
			//Punto 1
			System.out.println();
			System.out.println("1. La cantidad total de personas registradas es de: " + personas.size());
			
			
			//Punto 2
			List<Persona> racing = personas.stream()
					.filter(persona -> persona.getEquipo().equalsIgnoreCase("Racing"))
					.collect(Collectors.toList());
			for(Persona p : racing) {
				sumEdades = sumEdades + p.getEdad();
			}
			
			System.out.println();
			System.out.println("2. El promedio de edad de los socios de Racing es de: " + String.format("%.2f",sumEdades/racing.size()) + " años");
			
			
			//Punto 3
			List<Persona> casadosUni = personas.stream()
					.filter(persona -> persona.getEstadoCivil().equalsIgnoreCase("Casado"))
					.filter(persona -> persona.getNivelEstudios().equalsIgnoreCase("Universitario"))
					.collect(Collectors.toList());
			
			List<Persona> casadosUniDos = new ArrayList<Persona>();
			
			for(Persona p : casadosUni) {
				if (countCien < 100) {
					casadosUniDos.add(p);
					countCien ++;
				}else {
					break;
				}
			}
			
			List<Persona> casadosUniOrdenados = casadosUniDos.stream()
					.sorted(Comparator.comparing(Persona::getEdad))
					.collect(Collectors.toList());
			
			String format = "%-5s%s%n";
			countCien = 0;
			
			System.out.println();
			System.out.println("3. Las primeras 100 personas casadas, con estudios Universitarios, ordenadas por edad son: ");
			
			for(Persona p : casadosUniOrdenados) {
				countCien ++;
				System.out.printf(format, "", countCien + "° " + p.mostrarPuntoTres());
			}
			
			
			//Punto 4
			Map<String, List<Persona>> groupByNombre = personas.stream()
					.filter(persona -> persona.getEquipo().equalsIgnoreCase("River"))
					.collect(Collectors.groupingBy(Persona::getNombre));
			
			
			
			groupByNombre.forEach((nombre, personas1) -> {
				hashRiver.put(nombre, personas1.size());
			});
			
			for (String i : hashRiver.keySet()) {
				listaPuntoCuatro.add(hashRiver.get(i));
			}
			
			Set<Integer> setPuntoCuatro = new HashSet<>(listaPuntoCuatro);
			List<Integer> listaDosPuntoCuatro = new ArrayList<>(setPuntoCuatro);
			Collections.sort(listaDosPuntoCuatro);
			Collections.reverse(listaDosPuntoCuatro);
			
			System.out.println();
			System.out.println("4. Los 5 nombres más comunes entre los hinchas de River son: ");
			
			for (int value:listaDosPuntoCuatro) {
				for (String key : getKeys(hashRiver, value)) {
					if(countCinco<5) {
						countCinco ++;
						System.out.printf(format, "",countCinco + "° " + key + " (" + value + ")");
					}else {
						break;
					}
				}
			}
			
			
			//Punto 5
			for (String c : clubes) {
				Double promedio = personas.stream()
					.filter(persona -> persona.getEquipo().equalsIgnoreCase(c))
					.mapToDouble(Persona::getEdad)
					.average()
					.orElse(Double.NaN);
				
				listaPuntoCinco.add(promedio);
				
				personas.stream()
					.filter(persona -> persona.getEquipo().equalsIgnoreCase(c))
					.min(Comparator.comparing(Persona::getEdad))
					.ifPresent(persona -> {
						listaPuntoCinco.add(Double.valueOf(persona.getEdad()));
					});
				
				personas.stream()
					.filter(persona -> persona.getEquipo().equalsIgnoreCase(c))
					.max(Comparator.comparing(Persona::getEdad))
					.ifPresent(persona -> {
						listaPuntoCinco.add(Double.valueOf(persona.getEdad()));
					});
			}
			
			System.out.println();
			System.out.println("5. Listado de equipos con el promedio de edad de sus socios, la menor edad registrada y la mayor edad registrada: ");
			
			int countClub = 0;
			for (String c : clubes) {
				while (countClub == 0) {
					System.out.printf(format, "", "- Club: " + c + " Promedio Edad: " + String.format("%.2f",listaPuntoCinco.get(countClub)) + " Menor Edad: " + Math.round(listaPuntoCinco.get(countClub + 1)) + " Mayor Edad: " + Math.round(listaPuntoCinco.get(countClub + 2)) );
					listaPuntoCinco.remove(countClub);
					listaPuntoCinco.remove(countClub);
					listaPuntoCinco.remove(countClub);
					countClub ++;
				}
				countClub=0;
			}
			
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
	
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
