package se.miun.student.dt042g;
/*
 *  Servern skickar för att begära respons från klient
 */
public enum EnumRequestType {
	PLACEMENT, 
	MOVE,
	LOBBYRESPONSE, // Servern vill veta om spelaren vill spela ensam eller ej
	ABORTGAME, // Spelet dött pga omständigheter :)
}
