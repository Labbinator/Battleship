package se.miun.student.dt042g;
/*
 *  Servern skickar f�r att beg�ra respons fr�n klient
 */
public enum EnumRequestType {
	PLACEMENT, 
	MOVE,
	LOBBYRESPONSE, // Servern vill veta om spelaren vill spela ensam eller ej
	ABORTGAME, // Spelet d�tt pga omst�ndigheter :)
}
