package org.seasar.wicket.uifactory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Wicket���f���t�B�[���h�̐�����S2Wicket�Ɏw�����邱�Ƃ������A�m�e�[�V�����ł��B
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface WicketModel {

	/**
	 * ���f���̎�ʂ��w�肷�邽�߂̑����i�C�Ӂj�ł��B
	 * ���̑������ȗ������ꍇ�C{@link ModelType#PROPERTY}���K�p����܂��B
	 * @return ���f���̎��
	 */
	public ModelType type() default ModelType.PROPERTY;
	
}
