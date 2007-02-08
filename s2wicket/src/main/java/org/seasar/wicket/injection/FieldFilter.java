/*
 * $Id$
 * 
 * ==============================================================================
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

package org.seasar.wicket.injection;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * �w�肳�ꂽ�t�B�[���h�ɂ��āC���ꂪ�C���W�F�N�V�����Ώۂ��ǂ����𔻒f���C
 * �C���W�F�N�V�����Ώۂ������ꍇ�́C���b�N�A�b�v���邽�߂�Seasar�R���|�[�l���g����񋟂���
 * �������K�肵���C���^�t�F�[�X�ł��B<br />
 * <p>���̃C���^�t�F�[�X�́CS2Wicket���C���W�F�N�V�����ΏۂƂ���t�B�[���h���ǂ����𔻒f���鏈�����K�肳��Ă��܂��B
 * �J���҂́C���̃C���^�t�F�[�X�̎����N���X���쐬���邱�ƂŁC�Ǝ��ɃC���W�F�N�V�����ΏۂƂ���t�B�[���h�̔��f���S2Wicket��
 * �o�^���邱�Ƃ��ł��܂��B�܂��C�C���W�F�N�V�����ΏۂƔ��f�����t�B�[���h�ɂ��āC���̃t�B�[���h��
 * �C���W�F�N�V��������Seasar�R���e�i�Ǘ����̃R���|�[�l���g�I�u�W�F�N�g���C�ǂ̂悤�ȃR���|�[�l���g���Ń��b�N�A�b�v���邩�ɂ��Ă�
 * ���肵�܂��B</p>
 * <p>�Ⴆ�΁C������"Service"�ŏI����Ă���t�B�[���h���ɂ��ăC���W�F�N�V�����ΏۂƂ��C
 * Seasar�R���e�i����̃��b�N�A�b�v���̃R���|�[�l���g���Ƀt�B�[���h�����g�p����C�Ƃ������[����S2Wicket��
 * �o�^�������ꍇ�́C�ȉ��̂悤�Ȏ����N���X���쐬���܂��B</p>
 * <pre>
 * public class ServiceFieldFilter implements FieldFilter {
 *     public boolean isSupported(Field field) {
 *         String fieldName = field.getName();
 *         return fieldName.endsWith("Service");
 *     }
 *     public String getLookupComponentName(Field field) {
 *         return field.getName();
 *     }
 * }
 * </pre>
 * <p>��L�̎���t�B�[���h�t�B���^��S2Wicket�ɓo�^������@�́C�ȉ��̂Q����I���ł��܂��B
 * <ul>
 * <li>{@link SeasarComponentInjectionListener}�N���X�̃R���X�g���N�^�ɖ����I�ɓn���B</li>
 * <li>Seasar�R���e�i�ɓo�^����B</li>
 * </ul>
 * �ŏ��̕��@�́C�g�p����t�B�[���h�t�B���^��Java�\�[�X�R�[�h��Ŗ����I�ɃC���X�^���X��������ѓo�^���L�q��������ł��B
 * ����{@link AnnotationFieldFilter}�ɂ��{@link SeasarComponent}�A�m�e�[�V�������}�[�J�[�Ƃ���C���W�F�N�V����
 * ���s�������Ȃ��ꍇ�́C�ŏ��̕��@���̗p����K�v������܂��B��̓I�ɂ́C�ȉ��̂悤��{@link FieldFilter}�C���^�t�F�[�X��
 * �����I�u�W�F�N�g��{@link List}�R���N�V�����Ɋi�[���C{@link SeasarComponentInjectionListener}
 * �N���X�̃R���X�g���N�^�ɓn���܂��B</p>
 * <pre>
 * FieldFilter myFilter = new ServiceFieldFilter();
 * List<FieldFilter> filters = new ArrayList<FieldFilter>(1);
 * filters.add(myFilter);
 * addComponentInstantiationListener(
 *     new SeasarComponentInjectionListener(this, filters));
 * </pre>
 * <p>S2Wicket��{@link FieldFilter}�C���^�t�F�[�X�̎����I�u�W�F�N�g��Seasar�R���e�i��
 * �o�^���ꂽ�R���|�[�l���g�I�u�W�F�N�g���猟�����āC�����I�ɂ�����K�p���܂��B���ꂪ�Q�Ԗڂ̕��@�ł��B</p>
 * <pre>
 * &lt;components&gt;
 *     &lt;component name="myFilter"
 *         class="ServiceFieldFilter" /&gt;
 * &lt;/components&gt;
 * </pre>
 * <p>{@link SeasarComponentInjectionListener}�N���X�̃R���X�g���N�^�ɂ́C{@link FieldFilter}
 * �I�u�W�F�N�g�̃R���N�V�����������Ɏ����Ȃ����̂�����܂��̂ŁC���̕��@�ł͂�����g�p���܂��B
 * �����I�Ɏw�肷��ŏ��̕��@�ɔ�ׂāCSeasar�R���e�i�������{@link FieldFilter}�C���^�t�F�[�X�̎����I�u�W�F�N�g�̌����ɉ����āC
 * {@link AnnotationFieldFilter}�I�u�W�F�N�g���ÖٓI�ɓK�p����邱�Ƃɒ��ӂ���K�v������܂��B</p>
 * 
 * @see AnnotationFieldFilter
 * @author Yoichiro Tanaka
 * @since 1.1.0
 */
public interface FieldFilter extends Serializable {

	/**
	 * �w�肳�ꂽ�t�B�[���h���C���W�F�N�V�����̑ΏۂƂ��ăT�|�[�g����Ă��邩�ǂ�����Ԃ��܂��B
	 * @param field �t�B�[���h
	 * @return �T�|�[�g����Ă���� true
	 */
	public boolean isSupported(Field field);
	
	/**
	 * �w�肳�ꂽ�t�B�[���h�ɂ��āC���b�N�A�b�v����Seasar�R���|�[�l���g�̃R���|�[�l���g����Ԃ��܂��B
	 * ���̃��\�b�h�ɓn�����field�I�u�W�F�N�g�́C{@link #isSupported(Field)}���\�b�h�̌Ăяo�����ʂ�
	 * true �̃I�u�W�F�N�g�݂̂ƂȂ�܂��B
	 * ����Seasar�R���|�[�l���g���ł͂Ȃ��C�t�B�[���h�̌^�Ń��b�N�A�b�v���s���ꍇ�́Cnull��ԋp���Ă��������B
	 * @param field �t�B�[���h
	 * @return ���b�N�A�b�v����Seasar�R���|�[�l���g���B�����t�B�[���h�̌^�Ń��b�N�A�b�v����ꍇ�� null
	 */
	public String getLookupComponentName(Field field);
	
}
