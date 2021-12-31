package beans.sort;

import java.util.Comparator;

import dto.KorisnikDTO;

public class SortKorisnikDTOByFirstnameDescending implements Comparator<KorisnikDTO> {

	@Override
	public int compare(KorisnikDTO o1, KorisnikDTO o2) {
		// TODO Auto-generated method stub
		return o2.getIme().compareToIgnoreCase(o1.getIme());
	}

}
