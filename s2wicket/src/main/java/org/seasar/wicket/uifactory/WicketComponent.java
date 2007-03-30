/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.seasar.wicket.uifactory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.markup.html.WebPage;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.SubmitLink;
import wicket.markup.html.panel.Panel;
import wicket.model.BoundCompoundPropertyModel;
import wicket.model.IModel;
import wicket.model.PropertyModel;

/**
 * Wicket�R���|�[�l���g�̐�����S2Wicket�Ɏw�����邽�߂̃A�m�e�[�V�����ł��B<br />
 * <p>Wicket���g�p�����J���ł́C�y�[�W���\�����镡���̃R���|�[�l���g�𐶐����C�����e�R���|�[�l���g�ɓo�^����Ƃ����C
 * �R���|�[�l���g�c���[�̍\�z�������L�q����K�v������܂��B����́CSwing��SWT�ɑ�\�����GUI�A�v���P�[�V�����ɂ�����
 * ��ʂ̍\�z�����̋L�q�Ɣ��ɋ߂����̂ł��B�R���|�[�l���g�c���[�̍\�z�����̋L�q�́C�C���X�^���X�����Ɛe�R���|�[�l���g�ւ�
 * �o�^�����̌J��Ԃ��ł���C�K�v�ȋL�q�ʂ��������ɂ͓��e�͏璷�ł���C�ǐ������߂邱�Ƃ�����ł��B</p>
 * <p>{@link WicketComponent}�A�m�e�[�V�������g�p���邱�ƂŁC�ȉ��̏�����S2Wicket�ɍs�킹�邱�Ƃ��ł��܂��B</p>
 * <ul>
 * <li>�R���|�[�l���g�̃C���X�^���X�����B</li>
 * <li>���f���Ƃ̊֘A�t���B</li>
 * <li>�e�R���|�[�l���g�ւ̓o�^�B</li>
 * <li>�C�x���g�����̌Ăяo���B</li>
 * </ul>
 * <p>{@link WicketComponent}�A�m�e�[�V�������t�^���ꂽ�t�B�[���h�́C�܂��u"create"+[�t�B�[���h��]+"Component"�v�Ƃ���
 * �����K���̃��\�b�h���C�t�B�[���h����`���ꂽ�N���X�ɑ��݂��邩�ǂ����`�F�b�N���s���܂��B�������݂����ꍇ�ɂ́C
 * ���̃��\�b�h�̖߂�l���R���|�[�l���g�̃C���X�^���X�Ƃ��ăt�B�[���h�ɃZ�b�g����܂��B�Ⴆ�΁C</p>
 * <pre>
 * &#064;WicketComponent
 * private TextField firstName;
 * </pre>
 * <p>�Ƃ����t�B�[���h��`���������Ƃ���ƁC</p>
 * <pre>
 * public TextField createFirstNameComponent() {
 *     return new TextField("firstName");
 * }
 * </pre>
 * <p>�Ƃ����悤��createFirstNameComponent()���\�b�h���`���Ă������Ƃɂ���āCS2Wicket�ɂ�莩���I�ɌĂяo����āC
 * ���ʂ�firstName�t�B�[���h�ɃZ�b�g����܂��B</p>
 * <p>����create�`Component()���\�b�h����`����Ă��Ȃ������ꍇ�CS2Wicket�ɂ���ăR���|�[�l���g�̃C���X�^���X�������I�ɐ�������C
 * �ÖٓI�Ƀt�B�[���h�ɃZ�b�g����܂��B���̂Ƃ��Ɏg�p�����R���X�g���N�^�́C���f���Ɗ֘A�t���邩�ǂ����Ŏg�p����R���X�g���N�^���قȂ�܂��B
 * ���f���Ƃ̊֘A�t�����s��Ȃ��ꍇ�́Cwicket:id�݂̂��󂯎��R���X�g���N�^���K�p����܂��B
 * �֘A�t���郂�f����{@link WicketModel#type()}������{@link ModelType#RAW}���w�肳��Ă����ꍇ�́Cwicket:id
 * �����{@link WicketModel}�A�m�e�[�V�������t�^���ꂽ���f���t�B�[���h�̃I�u�W�F�N�g���󂯎�邱�Ƃ��ł���^�̈���������
 * �R���X�g���N�^���K�p����܂��B�܂��C{@link ModelType#RAW}�ȊO���w�肳�ꂽ���f���t�B�[���h�̃I�u�W�F�N�g�Ɗ֘A�t�����s���ꍇ�́C
 * wicket:id�����{@link IModel}�I�u�W�F�N�g���󂯎��R���X�g���N�^���K�p����܂��B</p>
 * <p>�R���|�[�l���g�̃C���X�^���X�𐶐�����ۂ�wicket:id������́C{@link #wicketId()}�����Ŏw�肳�ꂽ�����񂪓K�p����܂��B
 * ����{@link #wicketId()}�������ȗ������ꍇ�́C�t�B�[���h����wicket:id�Ƃ��ēK�p����܂��B�Ⴆ�΁C</p>
 * <pre>
 * &#064;WicketComponent(wicketId="firstName")
 * private TextField firstNameField;
 * &#064;WicketComponent
 * private TextField lastNameField;
 * </pre>
 * <p>�Ƃ����L�q�̏ꍇ�CfirstNameField�R���|�[�l���g��wicket:id��"firstName"���C
 * lastNameField�R���|�[�l���g��wicket:id��"lastNameField"���̗p����܂��B</p>
 * <p>���������R���|�[�l���g�́C�������Ǘ�����R���e�i�R���|�[�l���g�i{@link WebPage}�C{@link Panel}�C{@link Form}�Ȃǁj
 * �ɓo�^����K�v������܂��B{@link #parent()}�����ɐe�̃R���e�i�R���|�[�l���g�Ƃ���R���|�[�l���g�̃t�B�[���h�����w�肷�邱�ƂŁC
 * �e�R���|�[�l���g��{@link MarkupContainer#add(Component)}���\�b�h�ɃR���|�[�l���g���n����C�o�^����܂��B
 * �܂�C{@link #parent()}�����ɂ���āC�R���|�[�l���g�c���[�����肳��܂��B{@link #parent()}�����̎w�肪�ȗ����ꂽ�ꍇ�C
 * ���̃t�B�[���h����`���ꂽ�N���X�̃C���X�^���X���g���o�^�ΏۂƂȂ�܂��B�Ⴆ�΁C</p>
 * <pre>
 * public class InputPage extends WebPage {
 *     &#064;@WicketComponent
 *     private Form form;
 *     &#064;@WicketComponent(parent="form")
 *     private TextField firstNameField;
 * }
 * </pre>
 * <p>�Ƃ����L�q�̏ꍇ�CfirstNameField�R���|�[�l���g��form�R���e�i�R���|�[�l���g�ɓo�^����C
 * form�R���e�i�R���|�[�l���g��InputPage�y�[�W�R���|�[�l���g�ɓo�^����܂��B</p>
 * <p>�R���|�[�l���g�́C�ʏ퉽�炩�̃��f���I�u�W�F�N�g���邢�̓��f���I�u�W�F�N�g�̃v���p�e�B�𗘗p���܂��B
 * {@link #modelName()}���������{@link #modelProperty()}�������w�肷�邱�ƂŁC
 * �R���|�[�l���g�Ɋ֘A�t���郂�f���𖾎��I�Ɏw�肷�邱�Ƃ��ł��܂��B{@link #modelName()}������
 * ���f���Ƃ��Ċ֘A�t�����s������{@link WicketModel}�A�m�e�[�V�������t�^���ꂽ���f���t�B�[���h��
 * �t�B�[���h�����w�肵�܂��B����ɂ���āC�R���|�[�l���g�̃C���X�^���X�������ɁC�w�肳�ꂽ�t�B�[���h��
 * �Ɋ�Â��ă��f�����R���|�[�l���g�̃R���X�g���N�^�ɓn����Ċ֘A�t�����܂��B</p>
 * <p>����{@link #modelName()}�������ȗ������Ƃ��́C�ȉ��̏����œK�p���郂�f�����ÖٓI�Ɍ��肵�܂��B</p>
 * <ol>
 * <li>{@link #parent()}�����ɂ��e�R���|�[�l���g���w�肳��Ă��āC����ɐe�R���|�[�l���g�Ɋ֘A�t����ꂽ���f���t�B�[���h��
 * {@link WicketModel#type()}������{@link ModelType#RAW}�C{@link ModelType#BASIC}�C{@link ModelType#PROPERTY}
 * ���w�肳��Ă����Ƃ��́C���������w��Ɣ��f���C{@link UnsupportedOperationException}��O���X���[�����B</li>
 * <li>{@link #parent()}�����ɂ��e�R���|�[�l���g���w�肳��Ă��āC����ɐe�R���|�[�l���g�Ɋ֘A�t����ꂽ���f���t�B�[���h��
 * {@link WicketModel#type()}������{@link ModelType#COMPOUND_PROPERTY}���w�肳��Ă����Ƃ��́C
 * �R���|�[�l���g�ɑ΂��ă��f���̊֘A�t�����s��Ȃ��B</li>
 * <li>{@link #parent()}�����ɂ��e�R���|�[�l���g���w�肳��Ă��āC����ɐe�R���|�[�l���g�Ɋ֘A�t����ꂽ���f���t�B�[���h��
 * {@link WicketModel#type()}������{@link ModelType#BOUND_COMPOUND_PROPERTY}���w�肳��Ă����Ƃ��́C
 * �R���|�[�l���g�ɑ΂��ă��f���̊֘A�t���͍s��Ȃ����C�R���|�[�l���g��{@link #modelProperty()}�����Ŏw�肳�ꂽ
 * �v���p�e�B���ŁC{@link BoundCompoundPropertyModel#bind(Component, String)}�̌Ăяo���ɂ��C
 * �e�R���|�[�l���g�Ɋ֘A�t����ꂽ{@link BoundCompoundPropertyModel}�I�u�W�F�N�g�ƃo�C���h�����B</li>
 * <li>{@link #parent()}�����ɂ��e�R���|�[�l���g���w�肳��Ă��邪�����I�Ƀ��f�������w�肳��Ă��Ȃ��ꍇ�C
 * ���邢��{@link #parent()}�������ȗ�����Đe�R���|�[�l���g���w�肳��Ă��Ȃ��ꍇ�ŁC
 * �����{@link WicketModel}�A�m�e�[�V�����ɂ�郂�f���t�B�[���h���P�������݂��Ă���Ƃ��́C
 * �ȉ��̓�����s���B
 * <ul>
 * <li>���f���t�B�[���h��{@link WicketModel#type()}������{@link ModelType#RAW}�܂���{@link ModelType#BASIC}
 * ���w�肳��Ă����Ƃ��́C�R���|�[�l���g�ɑ΂��ă��f���̊֘A�t�����s��Ȃ��B</li>
 * <li>���f���t�B�[���h��{@link WicketModel#type()}������{@link ModelType#PROPERTY}���w�肳��Ă����Ƃ��́C
 * {@link #modelProperty()}�����Ŏw�肳�ꂽ�v���p�e�B���ŃR���|�[�l���g�Ɗ֘A�t�����s���B
 * {@link #modelProperty()}�������ȗ����ꂽ�ꍇ�́C�R���|�[�l���g�̃t�B�[���h�����v���p�e�B���Ƃ��ēK�p�����B</li>
 * <li>���f���t�B�[���h��{@link WicketModel#type()}������{@link ModelType#COMPOUND_PROPERTY}�܂���
 * {@link ModelType#BOUND_COMPOUND_PROPERTY}���w�肳�ꂽ�Ƃ��́C���������w��Ɣ��f���C{@link UnsupportedOperationException}
 * ��O���X���[�����B</li>
 * </ul>
 * </li>
 * </ol>
 * <p>{@link WicketComponent}�A�m�e�[�V�������t�^���ꂽ�t�B�[���h�̌^�����ۃN���X�̏ꍇ�C
 * �������̃��\�b�h�ɑ΂��ĊJ���҂͎�����^����K�v������܂��B{@link WicketComponent}�A�m�e�[�V������
 * �t�^���ꂽ�R���|�[�l���g�́C�u[���\�b�h��]+[�t�B�[���h��]�v�Ƃ��������K���ɏ]���Č��肳��郁�\�b�h���C
 * ���ۃ��\�b�h�̌Ăяo�����Ɏ��s���܂��B���̃��\�b�h�̈�����߂�l�̌^�́C
 * �I���W�i���̒��ۃ��\�b�h�ƈ�v���Ă���K�v������܂��B�Ⴆ�΁C{@link SubmitLink}
 * 
 * @see WicketAction
 * @see WicketModel
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface WicketComponent {
	
	/**
	 * ��������R���|�[�l���g��wicket:id���w�肷�邽�߂̑����i�C�Ӂj�ł��B
	 * ���̑������ȗ������ꍇ�C���̃A�m�e�[�V�������t�^���ꂽ�t�B�[���h�̖��O��wicket:id�Ƃ��č̗p����܂��B
	 * @return wicket:id�Ƃ��镶����
	 */
	public String wicketId() default "";
	
	/**
	 * ��������R���|�[�l���g��o�^����e�̃R���e�i�R���|�[�l���g�̃t�B�[���h�����w�肷�邽�߂̑����i�C�Ӂj�ł��B
	 * ���̑������ȗ������ꍇ�C�܂���"this"���w�肵���ꍇ�́C
	 * ���̃A�m�e�[�V�������t�^���ꂽ�t�B�[���h����������R���|�[�l���g���e�R���e�i�R���|�[�l���g�Ƃ��č̗p����܂��B
	 * @return �e�̃R���e�i�R���|�[�l���g�̃t�B�[���h��
	 */
	public String parent() default "";
	
	/**
	 * ��������R���|�[�l���g�Ɗ֘A�t�����s�����f���I�u�W�F�N�g�̃t�B�[���h�����w�肷�邽�߂̑����i�C�Ӂj�ł��B
	 * 
	 * @return
	 */
	public String modelName() default "";
	
	public String modelProperty() default "";
	
	public WicketAction[] actions() default {};
	
}
