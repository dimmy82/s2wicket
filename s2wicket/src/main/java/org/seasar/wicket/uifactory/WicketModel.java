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
import wicket.model.BoundCompoundPropertyModel;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.PropertyModel;

/**
 * Wicket���f���t�B�[���h�̐�����S2Wicket�Ɏw�����邱�Ƃ������A�m�e�[�V�����ł��B<br />
 * <p>Wicket�Œ񋟂����R���|�[�l���g�́C���炩�̃��f���I�u�W�F�N�g�Ɗ֘A�t�����܂��B
 * �R���|�[�l���g�́C�C�x���g�̔����Ȃǂ̎��ۂɂ��C���f���I�u�W�F�N�g�̓��e���擾���邢�͑�����s���܂��B
 * �R���|�[�l���g�ƃ��f���I�u�W�F�N�g���֘A�t������Ƃ��ɂ́C���f���I�u�W�F�N�g�����̂܂܃R���|�[�l���g��
 * �������ɓn���Ă��܂���i������܂����C�����̏ꍇ�͊֘A�t�����s�����߂�{@link IModel}�I�u�W�F�N�g
 * �Ń��f���I�u�W�F�N�g�����b�v���ăR���|�[�l���g�̃R���X�g���N�^�ɓn���܂��B{@link IModel}�N���X��
 * �����N���X�Ƃ��āC�ȉ��̃N���X��Wicket�ɕW���œ��ڂ���Ă��܂��B</p>
 * <ul>
 * <li>{@link Model} - ��{�I�ȃ��f����\�������N���X�B</li>
 * <li>{@link PropertyModel} - �v���p�e�B���R���|�[�l���g�Ɗ֘A�t����������������N���X�B</li>
 * <li>{@link CompoundPropertyModel} - wicket:id���v���p�e�B���Ƃ��ĕ����̎q�R���|�[�l���g�Ƃ̊֘A�t����������������N���X�B</li>
 * <li>{@link BoundCompoundPropertyModel} - {@link CompoundPropertyModel}�ɑ΂��āC�����I�ȃv���p�e�B���̎w��Ŋ֘A�t�����\�ɂ�������N���X�B</li>
 * </ul>
 * <p>{@link WicketModel}�A�m�e�[�V�����́C���ꂪ�t�^���ꂽ�t�B�[���h�����f���t�B�[���h�Ƃ��ă}�[�L���O���܂��B
 * {@link WicketComponent}�A�m�e�[�V�����ɂ���Ďw�肳�ꂽ�R���|�[�l���g�t�B�[���h�̏����̍ۂɁC�֘A�t����
 * �K�v�����郂�f���I�u�W�F�N�g���C{@link WicketModel}�A�m�e�[�V�������t�^���ꂽ���f���t�B�[���h����擾����܂��B
 * ���̍ہC{@link #type()}�����Ŏw�肳�ꂽ��ʂɏ]���ă��f���I�u�W�F�N�g���K�؂ȃI�u�W�F�N�g�Ƀ��b�v����āC
 * �R���|�[�l���g�Ɋ֘A�t�����܂��B�ǂ̂悤�Ƀ��f���I�u�W�F�N�g���R���|�[�l���g�Ɗ֘A�t�����邩�́C
 * {@link ModelType}�N���X��Javadoc���������������B</p>
 * <p>{@link WicketModel}�A�m�e�[�V�������t�^���ꂽ�t�B�[���h�ɃZ�b�g����I�u�W�F�N�g�̐����́C�ȉ��̎菇�ōs���܂��B</p>
 * <ol>
 * <li>�ucreate+[�t�B�[���h��]�v�Ƃ��������K���Ɋ�Â��Ė������ꂽ���O�������\�b�h����`����Ă����ꍇ�́C
 * ���̃��\�b�h���Ăяo���āC���̌��ʂ̖߂�l�����f���I�u�W�F�N�g�Ƃ��ăt�B�[���h�ɃZ�b�g����B</li>
 * <li>{@link #exp()}�������w�肳��Ă����ꍇ�́C���̑����l�Ƃ��ď����ꂽOGNL����]�����C
 * ���̌��ʓ���ꂽ�l�����f���I�u�W�F�N�g�Ƃ��ăt�B�[���h�ɃZ�b�g����B</li>
 * <li>�t�B�[���h�̌^�̃f�t�H���g�R���X�g���N�^���g�p���ăC���X�^���X�𐶐����C�t�B�[���h�ɃZ�b�g����B</li>
 * </ol>
 * <p>�ŏ��̕��@�́C���f���I�u�W�F�N�g�̐������������G�Ȏ菇�ɂȂ�ȂǁC�J���҂������I�ɋL�q�������ꍇ�ɓK�p���܂��B
 * ���L�̃��X�g�͖����I�Ƀ��f���I�u�W�F�N�g�̐������\�b�h���`������ł��B</p>
 * <pre>
 * &#064;WicketModel
 * private ConditionModel conditionModel;
 * public ConditionModel createConditionModel() {
 *     ...
 * }
 * </pre>
 * <p>��`���郁�\�b�h�́C�������Ȃ��C�߂�l�����f���t�B�[���h�ɑ������ۂɖ��ƂȂ�Ȃ��^�ł���K�v������܂��B</p>
 * <p>create���\�b�h����`����Ă��Ȃ��ꍇ�́C{@link #exp()}�����Ŏw�肳�ꂽOGNL����]�����邱�Ƃɂ���āC
 * ���̕]�����ʂ��t�B�[���h�ɃZ�b�g����܂��B���L�̃��X�g�́CMasterDataProducer�N���X��static���\�b�h��
 * �Ăяo���ă��f���I�u�W�F�N�g�Ƃ����ł��B</p>
 * <pre>
 * &#064;WicketModel(type=ModelType.RAW
 *     exp="&#064;MasterDataProcuder&#064;getDivisionList()")
 * private List divisionList;
 * </pre>
 * <p>create���\�b�h���Ȃ��C{@link #exp()}�������w�肳��Ă��Ȃ������ꍇ�́C
 * �t�B�[���h�̌^�̃f�t�H���g�R���X�g���N�^���g����S2Wicket���ÖٓI�ɃC���X�^���X�𐶐����C
 * �t�B�[���h�ɃZ�b�g���܂��B</p>
 * <pre>
 * &#064;WicketModel
 * private ConditionModel conditionModel;
 * </pre>
 * <p>��L�̃��X�g�ł́C�unew ConditionModel();�v�̌��ʂ��t�B�[���h�ɃZ�b�g����܂��B</p>
 * <p>{@link WicketModel}�A�m�e�[�V�����ɑ΂��鏈���́C{@link Component}�N���X��
 * �f�t�H���g�R���X�g���N�^����Ăяo����܂��B����āC{@link WicketModel}�A�m�e�[�V������
 * �t�^���ꂽ�t�B�[���h����`���ꂽ�N���X�̑��̃t�B�[���h�ɂ��āC�E�ӂ̎������s�����O
 * �ƂȂ�܂��B�܂�C</p>
 * <pre>
 * &#064;WicketModel(exp="getConditionModel()")
 * private ConditionModel conditionModel = new ConditionModel();
 * </pre>
 * �Ƃ����L�q���s���ƁCgetConditionModel()���\�b�h�̕]�����ʂ�S2Wicket�ɂ����
 * conditionModel�t�B�[���h�ɃZ�b�g����܂����C���̌�t�B�[���h�̉E�ӂ̎������s����C
 * confitionModel�t�B�[���h�̒l���㏑������܂��̂ŁC���ӂ��K�v�ł��B</p>
 * 
 * @see WicketComponent
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
	
	/**
	 * ���f���I�u�W�F�N�g�𐶐�����OGNL�����w�肷�邽�߂̑����i�C�Ӂj�ł��B
	 * ���̑������ȗ������ꍇ�Ccreate���\�b�h�̎��s���ʂ܂��̓t�B�[���h�̌^�̃f�t�H���g�R���X�g���N�^���g���Đ��������C���X�^���X���t�B�[���h�ɃZ�b�g����܂��B
	 * @return ���f���I�u�W�F�N�g��]�����ʂƂ���OGNL��
	 */
	public String exp() default "";
	
}
