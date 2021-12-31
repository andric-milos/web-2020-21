package beans.sort;

import java.util.Comparator;

import dto.KorisnikDTO;

public class SortKorisnikDTOByUsernameAscending implements Comparator<KorisnikDTO> {

	@Override
	public int compare(KorisnikDTO o1, KorisnikDTO o2) {
		// TODO Auto-generated method stub
		return o1.getKorisnickoIme().compareToIgnoreCase(o2.getKorisnickoIme());
	}

}
