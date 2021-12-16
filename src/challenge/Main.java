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
			Map<String, Integer> hashEquipos = new HashMap<String, Integer>();
			Map<String, List<Double>> hashEdades = new HashMap<String, List<Double>>();
			
			//Array
			List<Persona> personas = new ArrayList<Persona>();
			List<Integer> listaRiver = new ArrayList<Integer>();
			List<Integer> listaEquipos = new ArrayList<Integer>();
			
			//Set
			Set<String> equipos = new HashSet<String>();
						
			
			while ((line = br.readLine()) != null) {
				String[] data = line.split(splitBy);
				
				Persona p = new Persona(data[0], Integer.parseInt(data[1]), data[2], data[3], data[4]);
				
				equipos.add(p.getEquipo());
				
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
			
			List<Persona> casadosUniCien = casadosUni.stream().limit(100).collect(Collectors.toList());
			
			List<Persona> casadosUniOrdenados = casadosUniCien.stream()
					.sorted(Comparator.comparing(Persona::getEdad))
					.collect(Collectors.toList());
			
			countCien = 0;
			
			System.out.println();
			System.out.println("3. Las primeras 100 personas casadas, con estudios Universitarios, ordenadas por edad son: ");
			System.out.printf("    %-6s%-12s%-12s %-12s\n", "N°", "Nombre", "Edad", "Equipo");
			
			for(Persona p : casadosUniOrdenados) {
				countCien ++;
				System.out.printf("    %-6s%-12s %-12d%-6s\n", countCien + "° ", p.getNombre(), p.getEdad(), p.getEquipo());
			}
			
			
			//Punto 4
			Map<String, List<Persona>> groupByNombre = personas.stream()
					.filter(persona -> persona.getEquipo().equalsIgnoreCase("River"))
					.collect(Collectors.groupingBy(Persona::getNombre));
			
			
			
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
			
			
			//Punto 5
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
			System.out.println("5. Listado de equipos con el promedio de edad de sus socios, la menor edad registrada y la mayor edad registrada: ");
			
			Map<String, List<Persona>> groupByEquipo = personas.stream()
					.collect(Collectors.groupingBy(Persona::getEquipo));
			
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
