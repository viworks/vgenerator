package cn.viworks.vgenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VgException extends Exception {
	public VgException(Throwable t){
		super(t);
	}
}
