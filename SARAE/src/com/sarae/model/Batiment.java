package com.sarae.model;

import java.io.File;
import java.util.Vector;

import android.graphics.Bitmap;


//CLASSE BATIMENT
public class Batiment {	
	public int id_batiment;
	public float superficie,hauteur,largeur,profondeur,degrePenteToit,tauxVulnera;
	public String libelle,typeMateriaux,typePenteToit,typeMatToit;
	public Bitmap docPDF;
	public Position position;
	public Vector<Niveau> niveaux;
	
	public Batiment() {
		id_batiment=-1;
		
		hauteur=-1;
		largeur=-1;
		profondeur=-1;
		superficie=largeur*profondeur;
		degrePenteToit=0;
		tauxVulnera=0;
		
		libelle="Sans nom";
		typeMateriaux="Non définie";
		typePenteToit="Non définie";
		typeMatToit="Non définie";
		
		docPDF=null;
		position=new Position();
		niveaux=new Vector<Batiment.Niveau>();
	}
	
	public void copy(final Batiment b) {
		id_batiment=b.id_batiment;
		hauteur=b.hauteur;
		largeur=b.largeur;
		profondeur=b.profondeur;
		superficie=b.superficie;
		degrePenteToit=b.degrePenteToit;
		tauxVulnera=b.tauxVulnera;
		
		libelle=b.libelle;
		typeMateriaux=b.typeMateriaux;
		typePenteToit=b.typePenteToit;
		typeMatToit=b.typeMatToit;
		
		docPDF=b.docPDF;
		position=b.position;
		niveaux=b.niveaux;
	}
	
	public Batiment(int id_batiment, float hauteur, float largeur, float profondeur,
			         float degrePenteToit, float tauxVulnera, String libelle, String typeMateriaux,
			         String typePenteToit, String typeMatToit, Bitmap docPDF, Position position, Vector<Niveau> niveaux)
	{
		this.id_batiment=id_batiment;
		this.hauteur=hauteur;
		this.largeur=largeur;
		this.profondeur=profondeur;
		this.superficie=largeur*profondeur;
		superficie=largeur*profondeur;
		this.degrePenteToit=degrePenteToit;
		this.tauxVulnera=tauxVulnera;
		
		this.libelle=libelle;
		this.typeMateriaux=typeMateriaux;
		this.typePenteToit=typePenteToit;
		this.typeMatToit=typeMatToit;
		
		this.docPDF=docPDF;
		this.position=position;
		this.niveaux=niveaux;
	}
	
	public int getNbNiveaux()
	{return niveaux.size();}
	
	// CLASSE NIVEAU
	public class Niveau{
		public int id_Niveau,numEtage, nombrePieces;
		public Bitmap plan2D;
		public Vector<CodeEtare> codes = new Vector<CodeEtare>();
		public Niveau()	{}
		public Niveau(int numEtage, int id_niveau,int nombrePieces, Bitmap plan2D, Vector<CodeEtare> codes)
		{
			this.id_Niveau=id_niveau;
			this.numEtage=numEtage;
			this.nombrePieces=nombrePieces;
			this.plan2D=plan2D;
			this.codes=codes;
		}
		
		public void copy(final Niveau niveau)
		{
			id_Niveau=niveau.id_Niveau;
			numEtage=niveau.numEtage;
			nombrePieces=niveau.nombrePieces;
			plan2D=niveau.plan2D;
			codes=new Vector<CodeEtare>(niveau.codes);
		}
		// CLASSE CODEETARE
		public class CodeEtare{
			public String codeEtare;
			public Bitmap logo;
			public CodeEtare(){}
			public CodeEtare(String codeEtare, Bitmap logo)
			{
				this.codeEtare=codeEtare;
				this.logo=logo;
			}
			public void copy(CodeEtare code)
			{
				codeEtare=code.codeEtare;
				logo=code.logo;
			}
		}
	}
}
