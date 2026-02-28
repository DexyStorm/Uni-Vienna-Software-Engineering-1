package network;

import java.io.StringWriter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;

/**
 * the Translator only marshals the objects conversion will be done by the
 * Converter.
 * 
 * marshalling is done so that the server can accept whatever i send it
 */

public class Translator
{
	public StringWriter translateInternalToExternalHalfMap(PlayerHalfMap convertedHalfMap) throws JAXBException
	{
		StringWriter marshalledPlayerHalfMap = new StringWriter();
		JAXBContext jaxbContext;
		
		jaxbContext = JAXBContext.newInstance(PlayerHalfMap.class);
		
		Marshaller marshaller = jaxbContext.createMarshaller();
		
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		marshaller.marshal(convertedHalfMap, marshalledPlayerHalfMap);
		
		return marshalledPlayerHalfMap;
		
	}
	
	public StringWriter translateInternalToExternalMove(PlayerMove playerMove) throws JAXBException
	{
		StringWriter marshalledPlayerMove = new StringWriter();
		JAXBContext jaxbContext;
		
		jaxbContext = JAXBContext.newInstance(PlayerMove.class);
		
		Marshaller marshaller = jaxbContext.createMarshaller();
		
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		marshaller.marshal(playerMove, marshalledPlayerMove);
		
		return marshalledPlayerMove;
	}
}
