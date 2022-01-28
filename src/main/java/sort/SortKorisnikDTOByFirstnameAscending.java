package sort;

import java.util.Comparator;

import dto.KorisnikDTO;

public class SortKorisnikDTOByFirstnameAscending implements Comparator<KorisnikDTO> {

	@Override
	public int compare(KorisnikDTO o1, KorisnikDTO o2) {
		return o1.getIme().compareToIgnoreCase(o2.getIme());
	}

}
