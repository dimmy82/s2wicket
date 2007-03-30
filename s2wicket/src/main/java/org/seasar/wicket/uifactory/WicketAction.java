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

import wicket.Component;
import wicket.markup.html.link.Link;

/**
 * �R���|�[�l���g�̃C�x���g�n���h���̌Ăяo�����Ɏ��s�������������`���邽�߂̃A�m�e�[�V�����ł��B<br />
 * <p>Wicket���񋟂���e�R���|�[�l���g�ł́CWeb�u���E�U��ōs����C�x���g�i�t�H�[���̃T�u�~�b�g�⃊���N�̃N���b�N�Ȃǁj�ɉ����āC
 * �Ăяo����郁�\�b�h�����ꂼ���`����Ă��܂��B�����̃C�x���g�n���h�����\�b�h�́C���ۃ��\�b�h�Ƃ��Ē�`����Ă���̂ŁC
 * �Ⴆ��{@link Link#onClick()}���\�b�h���i�����N���X�Ȃǂ�����āj�������邱�ƂŁC�A�v���P�[�V�����Ǝ��̃C�x���g������
 * ��`���܂��B</p>
 * <p>S2Wicket�ł́C�R���|�[�l���g�ɑ΂���C�x���g�ɂ��āC�R���|�[�l���g���T�u�N���X�����邱�ƂȂ��C
 * �����K���ɂ���Č��肳���C�x���g�n���h�����\�b�h���Ăяo����܂��B�������C�����̃A�v���P�[�V�����ł́C�v���[���e�[�V�����w��
 * �s���鏈���́i�r�W�l�X���W�b�N���Ăяo���̂݁C�Ȃǁj�P���Ȃ��Ƃ������C���̂��߂����Ƀ��\�b�h���`���邱�Ƃ�
 * ���ʂɊ������ʂ����Ȃ�����܂���B</p>
 * <p>{@link WicketAction}�A�m�e�[�V�������g�p���邱�ƂŁC�R���|�[�l���g�ɔ��������C�x���g�ɂ��āC
 * �T�u�N���X���Ȃǂ��s�����ƂȂ��C���ȒP�ȋL�q�ŃC�x���g�������`���邱�Ƃ��ł��܂��B{@link WicketAction}�A�m�e�[�V�����ł́C
 * �ȉ��̃C�x���g�����@�\���񋟂���܂��B</p>
 * <ul>
 * <li>OGNL���ɂ��C�x���g�����̒�`�B</li>
 * <li>{@link Component#setResponsePage(Class)}�ɂ��y�[�W�J�ځB</li>
 * </ul>
 * <p>�ǂ̃C�x���g�n���h�����\�b�h�ɓK�p���邩���C{@link #method()}�����Ŏw�肵�܂��B���̑����ɂ́C
 * �K�p����C�x���g�n���h���̃��\�b�h�������̂܂܋L�q���܂��B�Ⴆ�΁C{@link Link#onClick()}���\�b�h�ɓK�p�������ꍇ�́C
 * �umethod="onClick"�v�ƋL�q���܂��B</p>
 * <p>�C�x���g�������Ɏ��s������OGNL���́C{@link #exp()}�����ɋL�q���܂��B�L�q����OGNL���́C�C�x���g�̔�������
 * �]������܂��B�C�x���g�n���h�����\�b�h���̂͏������ʂ�ԋp���Ȃ�void�Ȃ̂ŁCOGNL���̕]�����ʂɂ��ĉ��炩�̃t�B�[���h
 * �ɑ�������Ƃ������Ƃ͂���܂���B�Ⴆ�΁C���炩�̃r�W�l�X���W�b�N�̌Ăяo�����ʂ����f���t�B�[���h�ɑ������C
 * �Ƃ������Ƃ��������ꍇ�́C���f���t�B�[���h�ւ̑������OGNL���Ɋ܂߂�K�v������܂��B</p>
 * <p>{@link #exp()}�����Ŏw�肳�ꂽOGNL���́CS2Wicket���񋟂���R���e�L�X�g���ŕ]������܂��B
 * �R���e�L�X�g���̃��[�g�I�u�W�F�N�g�́C�����Ώۂ̃R���|�[�l���g���������Ă���I�u�W�F�N�g�ł��B�Ⴆ�΁C</p>
 * <pre>
 * public class InputPage extends WebPage {
 *     &#064;WicketComponent(actions={
 *             &#064;WicketAction(method="onClick", exp="logic1()")
 *         }
 *     }
 *     private Link link;
 *     public void logic1() {
 *         // �r�W�l�X���W�b�N
 *     }
 * }
 * </pre>
 * <p>�Ƃ����L�q�̏ꍇ�́Clink�R���|�[�l���g����������InputPage�N���X�ɒ�`���ꂽlogic1()���\�b�h�����s����܂��B</p>
 * <p>�C�x���g�����̌��ʁC�ʂ̃y�[�W�ɑJ�ڂ������Ƃ��́C{@link #responsePage()}�������g�p���āC���y�[�W��
 * �w�肷�邱�Ƃ��ł��܂��B{@link #responsePage()}�����ɂ́C�J�ڂ������y�[�W�̃N���X�����L�q���܂��B
 * �L�q���ꂽ�N���X�����炻��{@link Class}�I�u�W�F�N�g���擾���C�����{@link Component#setResponsePage(Class)}���\�b�h��
 * �n���ăy�[�W�J�ڂ��s���܂��B���̍ہC�L�q���ꂽ�N���X���̃N���X�I�u�W�F�N�g���C�ȉ��̏��Ō������܂��B</p>
 * <ol>
 * <li>{@link Class#forName(String)}���\�b�h�ɁC�L�q���ꂽ�N���X���iFQCN�ŋL�q���ꂽ���Ƃ�z��j�����̂܂ܓn���āC�N���X�I�u�W�F�N�g�𓾂�B</li>
 * <li>�R���|�[�l���g����������y�[�W�N���X�̃p�b�P�[�W�����L�q���ꂽ�N���X���̐擪�ɒǉ����C
 * �����{@link Class#forName(String)}�ɓn���ăN���X�I�u�W�F�N�g�𓾂�B</li>
 * </ol>
 * <p>�Ⴆ�΁C</p>
 * <pre>
 * package pkg1;
 * public class InputPage extends WebPage {
 *     &#064;WicketComponent(actions={
 *             &#064;WicketAction(method="onClick", responsePage="ConfirmPage")
 *         }
 *     }
 *     private Link link;
 * }
 * </pre>
 * <p>�Ƃ����L�q�̏ꍇ�́Cpkg1.ConfirmPage�N���X���J�ڂ���y�[�W�̃N���X�Ƃ��ēK�p����܂��B</p>
 * <p>{@link WicketAction}�A�m�e�[�V�����ɂ����āC{@link #method()}�����͏ȗ����邱�Ƃ͂ł��܂���B
 * ����ɑ΂��āC{@link #exp()}���������{@link #responsePage()}�����͂ǂ�����ȗ����邱�Ƃ��ł��܂��B
 * �����w�肵���ꍇ�́C{@linkplain #exp()}�����Ŏw�肵��OGNL���̕]����ɁC{@link #responsePage()}
 * �����Ŏw�肳�ꂽ�y�[�W�J�ڂ̏������s���܂��B</p>
 * <p>OGNL���̕]�����ɉ��炩�̗�O�����������ꍇ�CS2Wicket�͑Ώۂ̃R���|�[�l���g����������N���X��handleException()
 * ���\�b�h���Ăяo���܂��BhandleException()���\�b�h�ł́C�ȉ��̈������`���܂��B</p>
 * <ul>
 * <li>��1���� - {@link Component}�i�C�x���g�����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g�j</li>
 * <li>��2���� - {@link String}�i�C�x���g�n���h�����\�b�h���j</li>
 * <li>��3���� - {@link Exception}�i����������O�I�u�W�F�N�g�j</li>
 * </ul>
 * <p>�J���҂�handleException()���\�b�h�̒��ŁC�����̏������ɓƎ��̗�O�������L�q���܂��B
 * handleException()���\�b�h���Ăяo���ꂽ�ꍇ�C{@link #responsePage()}�����Ŏw�肳�ꂽ�y�[�W�J�ڂ�
 * �s���܂���B��O�������ɕʃy�[�W�ɑJ�ڂ������ꍇ�́ChandleException()���\�b�h����{@link Component#setResponsePage(Class)}
 * ���\�b�h�̌Ăяo���Ȃǂ̋L�q���s���Ă��������B</p>
 * <p>{@link WicketAction}�A�m�e�[�V�����̎g�p����ȉ��Ɏ����܂��B</p>
 * <pre>
 * package emp;
 * public class CreateEmployeeConfirmPage extends WebPage {
 *     &#064;WicketModel
 *     private EmployeeModel employeeModel;
 *     ...
 *     &#064;WicketComponent(actions={
 *             &#064;WicketAction(
 *                 method="onSubmit",
 *                 exp="logic.create(employeeModel)"
 *                 responsePage="CreateEmployeeCompletePage")
 *         }
 *     )
 *     private Form form;
 *     &#064;SeasarComponent
 *     private EmployeeLogic logic;
 *     public void handleException(Component target, String methodName, Exception e) {
 *         if ((target == form) && (e instanceof DivisionNotFoundException)) {
 *             setResponsePage(SelectDivisionPage.class);
 *         } else {
 *             throw IllegalStateException("Unsupported exception", exception);
 *         }
 *     }
 * }
 * </pre>
 * <p>��L�ł́C�t�H�[���̃T�u�~�b�g���ɁCOGNL���Ŏw�肳�ꂽ����
 * �ilogic�I�u�W�F�N�g��create()���\�b�h��employeeModel�I�u�W�F�N�g��n���ČĂяo���j
 * �����s���C����I�������emp.CreateEmployeeCompletePage�N���X�̃y�[�W�ɑJ�ڂ��܂��B
 * ����logic�I�u�W�F�N�g��create()���\�b�h�Ăяo������DivisionNotFoundException��O�����������ꍇ�́C
 * emp.SelectDivisionPage�N���X�̃y�[�W�ɑJ�ڂ��C����ȊO�̏ꍇ�͕s��Ƃ���{@link IllegalStateException}
 * ��O���X���[���Ă��܂��B</p>
 * <p>{@link WicketAction}�A�m�e�[�V�������K�p����郁�\�b�h�́C</p>
 * <ul>
 * <li>���ۃ��\�b�h�ł���B</li>
 * <li>�X�R�[�v��protected�ł���C�߂�l�̌^��void�ł���B</li>
 * </ul>
 * <p>�̂ǂ��炩�̏����𖞂����Ă��郁�\�b�h�Ɍ���܂��B</p>
 * 
 * @see WicketComponent
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
public @interface WicketAction {
	
	/** �����ΏۂƂ���C�x���g�n���h�����\�b�h�̃��\�b�h�� */
	public String method();
	
	/** �C�x���g�������Ɏ��s�i�]���j������OGNL�� */
	public String exp() default "";
	
	/** ���ɑJ�ڂ������y�[�W�̃N���X�� */
	public String responsePage() default "";
	
}
