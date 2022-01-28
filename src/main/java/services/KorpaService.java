package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.Artikal;
import beans.ArtikalSaKolicinom;
import beans.Korisnik;
import beans.Korpa;
import beans.Restoran;
import enums.TipKorisnika;
import dao.KomentarDAO;
import dao.KorisnikDAO;
import dao.PorudzbinaDAO;
import dao.RestoranDAO;
import dao.ZahtevZaDostavuDAO;
import dto.ArtikalSaKolicinomDTO;

@Path("/cart")
public class KorpaService {
	
	@Context
	HttpServletRequest request;
	
	@Context
	ServletContext ctx;
	
	public KorpaService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (ctx.getAttribute("korisnici") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("korisnici", new KorisnikDAO(path));
		}
		
		if (ctx.getAttribute("restorani") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("restorani", new RestoranDAO(path));
		}
		
		if (ctx.getAttribute("porudzbine") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("porudzbine", new PorudzbinaDAO(path));
		}
		
		if (ctx.getAttribute("zahtevi") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("zahtevi", new ZahtevZaDostavuDAO(path));
		}
		
		if (ctx.getAttribute("komentari") == null) {
			String path = ctx.getRealPath("");
			ctx.setAttribute("komentari", new KomentarDAO(path));
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addToCart(ArtikalSaKolicinomDTO artikalDTO) {
		/* podaci o artiklu koji stizu sa klijenta su samo: restoran, naziv artikla i koliko
		 * puta je artikal dodat u korpu
		 * klasa: ArtikalSaKolicinomDTO
		*/
		
		// 1) dobaviti korpu iz sesije ili je inicijalizovati
		Korpa korpa = (Korpa) request.getSession().getAttribute("korpa");
		
		if (korpa == null) {
			Korisnik korisnik = (Korisnik) request.getSession().getAttribute("korisnik");
			
			if (korisnik == null) {
				korpa = new Korpa();
			} else {
				// proveriti mozda prvo da li korisnik vec ima korpu u sacuvanim korpama?
				// ako korisnik ima sacuvanu korpu, znaci da se izlogovao ili je zavrsio sesiju, a imao je nesto u korpi
				
				if (!korisnik.getTipKorisnika().equals(TipKorisnika.KUPAC)) {
					return Response.status(Status.BAD_REQUEST).entity("NOT A CUSTOMER").build();
				}
				
				korpa = new Korpa(korisnik.getKorisnickoIme());
			}
		}
		
		// 2) dodavanje artikla u korpu
		RestoranDAO restoranDAO = (RestoranDAO) ctx.getAttribute("restorani");
		Restoran restoran = restoranDAO.getRestaurantByItsName(artikalDTO.getRestoran());
		
		if (restoran == null) {
			return Response.status(Status.BAD_REQUEST).entity("RESTAURANT DOES NOT EXIST").build();
		}
		
		Artikal artikal = restoranDAO.getArtikalByItsName(restoran, artikalDTO.getNaziv());
		
		if (artikal == null) {
			return Response.status(Status.BAD_REQUEST).entity("ARTICLE DOES NOT EXIST").build();
		}
		
		if (artikalDTO.getKoliko() < 1) {
			return Response.status(Status.BAD_REQUEST).entity("INVALID AMOUNT").build();
		}
		
		// dobavi iz kog restorana su artikli iz korpe
		String restoranIzKorpe;
		if (korpa.getArtikli().isEmpty()) {
			restoranIzKorpe = null;
		} else {
			restoranIzKorpe = korpa.getArtikli().get(0).getArtikal().getRestoran();
		}
		
		// svi artikli moraju biti iz istog restorana
		if (restoranIzKorpe != null && !restoran.getNaziv().equals(restoranIzKorpe)) {
			return Response.status(Status.BAD_REQUEST).entity("CAN'T MIX RESTAURANTS").build();
		}
		
		// ako je artikal vec dodat u korpu ranije, samo povecati broj puta koliko je dodat
		// if ()
		for (ArtikalSaKolicinom a : korpa.getArtikli()) {
			if (a.getArtikal().getNaziv().equals(artikal.getNaziv())) {
				a.setKoliko(a.getKoliko() + artikalDTO.getKoliko());
				korpa.setCena(korpa.getCena() + artikal.getCena() * artikalDTO.getKoliko());
				
				return Response.status(Status.OK).build();
			}
		}
		
		ArtikalSaKolicinom noviArtikalSaKolicinom = new ArtikalSaKolicinom(
			artikal,
			artikalDTO.getKoliko()
		);
		
		korpa.getArtikli().add(noviArtikalSaKolicinom);
		korpa.setCena(korpa.getCena() + artikal.getCena() * artikalDTO.getKoliko());
		request.getSession().setAttribute("korpa", korpa);
		
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getShoppingCart() {
		Korpa korpa = (Korpa) request.getSession().getAttribute("korpa");
		
		if (korpa == null) {
			return Response.status(Status.OK).entity(new Korpa()).build();
		}
		
		return Response.status(Status.OK).entity(korpa).build();
	}
	
	@GET
	@Path("/numberOfArticles")
	public Response getNumberOfArticlesInShoppingCart() {
		Korpa korpa = (Korpa) request.getSession().getAttribute("korpa");
		
		if (korpa == null) {
			return Response.status(Status.OK).entity(0).build();
		}
		
		return Response.status(Status.OK).entity(korpa.getArtikli().size()).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeFromCart(ArtikalSaKolicinomDTO artikalDTO) {
		/* podaci o artiklu koji stizu sa klijenta su samo: restoran, naziv artikla
		 * klasa: ArtikalSaKolicinomDTO
		*/
		
		Korpa korpa = (Korpa) request.getSession().getAttribute("korpa");
		
		if (korpa == null) {
			return Response.status(Status.BAD_REQUEST).entity("CART IS EMPTY").build();
		}
		
		RestoranDAO restoranDAO = (RestoranDAO) ctx.getAttribute("restorani");
		Restoran restoran = restoranDAO.getRestaurantByItsName(artikalDTO.getRestoran());
		
		if (restoran == null) {
			return Response.status(Status.BAD_REQUEST).entity("RESTAURANT DOES NOT EXIST").build();
		}
		
		
		if (korpa.getArtikli().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity("CART IS EMPTY").build();
		}
		
		String restoranIzKorpe = korpa.getArtikli().get(0).getArtikal().getRestoran();
		if (!artikalDTO.getRestoran().equals(restoranIzKorpe)) {
			return Response.status(Status.BAD_REQUEST).entity("WRONG RESTAURANT").build();
		}
		
		Artikal artikal = restoranDAO.getArtikalByItsName(restoran, artikalDTO.getNaziv());
		
		if (artikal == null) {
			return Response.status(Status.BAD_REQUEST).entity("ARTICLE DOES NOT EXIST").build();
		}
		
		for (ArtikalSaKolicinom a : korpa.getArtikli()) {
			if (a.getArtikal().getNaziv().equals(artikalDTO.getNaziv())) {
				korpa.getArtikli().remove(a);
				korpa.setCena(korpa.getCena() - a.getKoliko() * artikal.getCena());
				// request.getSession().setAttribute("korpa", korpa);
				return Response.status(Status.OK).build();
			}
		}
		
		return Response.status(Status.BAD_REQUEST).entity("ARTICLE IS NOT IN THE CART").build();
	}
	
	@PUT
	@Path("/updateArticleAmount")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateArticleAmount(ArtikalSaKolicinomDTO artikalDTO) {
		/* podaci o artiklu koji stizu sa klijenta su samo: naziv artikla i koliko
		 * klasa: ArtikalSaKolicinomDTO
		*/
		
		Korpa korpa = (Korpa) request.getSession().getAttribute("korpa");
		
		if (korpa == null) {
			return Response.status(Status.BAD_REQUEST).entity("CART IS EMPTY").build();
		}
		
		if (korpa.getArtikli().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity("CART IS EMPTY").build();
		}
		
		for (ArtikalSaKolicinom a : korpa.getArtikli()) {
			if (a.getArtikal().getNaziv().equals(artikalDTO.getNaziv())) {
				korpa.setCena(korpa.getCena() + a.getArtikal().getCena() * (artikalDTO.getKoliko() - a.getKoliko()));
				a.setKoliko(artikalDTO.getKoliko());
				
				// request.getSession().setAttribute("korpa", korpa);
				return Response.status(Status.OK).build();
			}
		}
		
		return Response.status(Status.BAD_REQUEST).entity("ARTICLE IS NOT IN THE CART").build();
	}

}
