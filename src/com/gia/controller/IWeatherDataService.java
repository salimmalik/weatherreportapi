package com.gia.controller;

/**
 * 
 *
 *         An Inteface representing IWeatherDataService object Throws a
 *         WeatherDataServiceException
 */
public interface IWeatherDataService
{
	public WeatherData getWeatherData(Location location) throws WeatherDataServiceException;
}
