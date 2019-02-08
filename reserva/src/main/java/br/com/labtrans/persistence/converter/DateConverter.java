/*
 * DateConverter.java
 * Copyright (c) LabTrans/UFSC.
 *
 * Este software é confidencial e propriedade da LabTrans/UFSC.
 * Não é permitida sua distribuição ou divulgação do seu conteúdo sem expressa autorização da LabTrans/UFSC.
 * Este arquivo contém informações proprietárias.
 * 
 * @author Ludemeula Fernandes.
 */
package br.com.labtrans.persistence.converter;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Specializes;
import javax.json.Json;

import br.com.caelum.vraptor.serialization.gson.DateGsonConverter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Classe parar conversão de {@link Json} para {@link Date}.
 *
 * @author Ludemeula Fernandes
 */
@Dependent 
@Specializes
public class DateConverter extends DateGsonConverter implements JsonSerializer<Date>, JsonDeserializer<Date> {

	private final SimpleDateFormat iso8601Format;

	/**
	 * Contrutor da classe.
	 *
	 */
	public DateConverter() {
		this.iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	}

	/**
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object,
	 *      java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
		String dateString = iso8601Format.format(date);
		return new JsonPrimitive(dateString);
	}

	/**
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement,
	 *      java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Date data = null;
		
		try {
			data = iso8601Format.parse(json.getAsString());
		} catch (ParseException e) {
			/* Caso exista casos no sistema com data JSON com o formado: "yyyy-MM-dd'T'HH:mm:ssZ". */
			data = super.deserialize(json, typeOfT, context);
		}
		
		return data;
	}
}