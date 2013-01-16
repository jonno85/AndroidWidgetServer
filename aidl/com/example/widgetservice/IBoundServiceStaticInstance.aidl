package com.example.widgetservice;

import com.example.widgetservice.IGeneratedValueListener;
import com.example.widgetservice.GeneratedValueResult;

interface IBoundServiceStaticInstance {
	void putSeed(int seed);
	void setServiceRunning(boolean value);

	IBoundServiceStaticInstance getInstance();

	GeneratedValueResult getCurrentResult();
	void addListener(in IGeneratedValueListener listener);
	void removeListener(in IGeneratedValueListener listener);
}