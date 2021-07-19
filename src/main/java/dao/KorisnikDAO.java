package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import beans.Korisnik;
import beans.Pol;
import beans.TipKorisnika;

public class KorisnikDAO {
	private HashMap<String, Korisnik> korisnici;	// key = korisnicko ime; value = objekat (korisnik)
	private String contextPath;
	
	/* contextPath je parametar koji prosledjujemo u servletu tj. u service klasi i predstavlja
	 * putanju do aplikacije u tomcat-u
	 */
	public KorisnikDAO(String contextPath) {
		this.korisnici = new HashMap<String, Korisnik>();
		this.contextPath = contextPath;
		// ucitajKorisnike(contextPath);
		
		// dodavanje korisnika kako bih mogao da istestiram login/logout
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = dateFormat.parse("09-06-1997");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		korisnici.put("proba", new Korisnik("proba", "proba", "proba", "proba", Pol.MUSKO, date, TipKorisnika.KUPAC));
		korisnici.put("admin", new Korisnik("admin", "admin", "admin", "admin", Pol.MUSKO, date, TipKorisnika.ADMINISTRATOR));
		korisnici.put("dostavljac", new Korisnik("dostavljac", "dostavljac", "dostavljac", "dostavljac", Pol.MUSKO, date, TipKorisnika.DOSTAVLJAC));
		korisnici.put("menadzerka", new Korisnik("menadzerka", "menadzerka", "menadzerka", "menadzerka", Pol.ZENSKO, date, TipKorisnika.MENADZER));
	}
	
	/* ucitava korisnike iz web-2020-21/data/korisnici.txt fajla i dodaje ih u hashmap-u
	 * kljuc hashmap-e je username korisnika
	 * u fajlu korisnici.txt jedan red predstavlja jednog korisnika
	 * svaki red je oblika: korisnickoIme;lozinka;ime;prezime;pol;datumRodjenja(dd-MM-yyyy);tipKorisnika
	 * primer: mj;goat;michael;jordan;MUSKO;09-06-1997;KUPAC
	 */
	private void ucitajKorisnike(String contextPath) {
		String path = contextPath + "data\\korisnici.txt";
		
		BufferedReader in = null;
		
		try {
			File file = new File(path);
			in = new BufferedReader(new FileReader(file));
			
			String line;
			StringTokenizer st;
			
			while ((line = in.readLine()) != null) {
				line = line.trim(); // eliminates leading and trailing spaces
				
				if (line.equals("") || line.indexOf('#') == 0)  // line.indexOf('#') == 0 why???
					continue;
				
				st = new StringTokenizer(line, ";");
				
				while (st.hasMoreTokens()) {
					String korisnickoIme = st.nextToken().trim();
					String lozinka = st.nextToken().trim();
					String ime = st.nextToken().trim();
					String prezime = st.nextToken().trim();
					
					String polString = st.nextToken().trim();
					Pol pol = Pol.stringToPol(polString);
					
					String datumString = st.nextToken().trim();
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Date datum = dateFormat.parse(datumString);
					
					String tipKorisnikaString = st.nextToken().trim();
					TipKorisnika tipKorisnika = TipKorisnika.stringToTipKorisnika(tipKorisnikaString);
					
					korisnici.put(korisnickoIme, new Korisnik(korisnickoIme, lozinka, ime, prezime, pol, datum, tipKorisnika));
				}
			}
		} catch (NullPointerException e) {
			System.out.println("Pathname argument is null!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("File " + path + " doesn't exist!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Date couldn't be parsed!");
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				System.out.println("Couldn't close the FileReader!");
				e.printStackTrace();
			}
		}
		
	}
	
	public void sacuvajKorisnike(String contextPath) {
		String path = contextPath + "data\\korisnici.txt";
		
		FileWriter fileWriter = null;
		
		try {
			File file = new File(path);
			fileWriter = new FileWriter(file);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			
			for (Korisnik korisnik : korisnici.values()) {
				String line = korisnik.getKorisnickoIme() + ";" + 
							  korisnik.getLozinka() + ";" + 
							  korisnik.getIme() + ";" + 
							  korisnik.getPrezime() + ";" + 
							  Pol.polToString(korisnik.getPol()) + ";" +
							  dateFormat.format(korisnik.getDatumRodjenja()) + "\n";
				
				fileWriter.write(line);
			}
		} catch (NullPointerException e) {
			// u 2 scenarija moze baciti NullPointerException:
			// 		1) Pathname argument is null prilikom new File(path)
			// 		2) Given date is null prilikom dateFormat.format
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Problem with opening the FileWriter!");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void add(Korisnik korisnik) {
		if (!korisnici.containsKey(korisnik.getKorisnickoIme())) {
			korisnici.put(korisnik.getKorisnickoIme(), korisnik);
			sacuvajKorisnike(contextPath);
		}
	}
	
	public Collection<Korisnik> getAll() {
		return korisnici.values();
	}
	
	public HashMap<String, Korisnik> getKorisniciHashMap() {
		return korisnici;
	}

}
