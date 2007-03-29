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
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.TextField;
import wicket.markup.html.link.PageLink;
import wicket.markup.html.list.ListView;
import wicket.model.BoundCompoundPropertyModel;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.PropertyModel;

/**
 * ���f���̎�ʂ��`�����񋓌^�ł��B<br />
 * <p>���̗񋓌^�́C{@link WicketModel}�A�m�e�[�V�����ɂ���Ďw�肳��郂�f���I�u�W�F�N�g�ƁC
 * ���f���I�u�W�F�N�g���g�p����R���|�[�l���g�Ƃ̊ԂŁC�ǂ̂悤�Ɋ֘A�t�����s�������w�肷�邽�߂Ɏg�p���܂��B</p>
 * <p>�e�t�B�[���h�ɂ��āC�ǂ̂悤�Ȋ֘A�t���ƂȂ邩���ȉ��Ɏ����܂��B</p>
 * <ul>
 * <li>{@link #RAW} - ���f���I�u�W�F�N�g�����̂܂܃R���|�[�l���g�Ɋ֘A�t�����܂��B</li>
 * <li>{@link #BASIC} - ���f���I�u�W�F�N�g��{@link Model}�I�u�W�F�N�g�Ƃ��ă��b�v����ăR���|�[�l���g�Ɋ֘A�t�����܂��B</li>
 * <li>{@link #PROPERTY} - ���f���I�u�W�F�N�g��{@link PropertyModel}�I�u�W�F�N�g�Ƃ��ă��b�v����ăR���|�[�l���g�Ɋ֘A�t�����܂��B</li>
 * <li>{@link #COMPOUND_PROPERTY} - ���f���I�u�W�F�N�g��{@link CompoundPropertyModel}�I�u�W�F�N�g�Ƃ��ă��b�v����ăR���|�[�l���g�Ɋ֘A�t�����܂��B</li>
 * <li>{@link #BOUND_COMPOUND_PROPERTY} - ���f���I�u�W�F�N�g��{@link BoundCompoundPropertyModel}�I�u�W�F�N�g�Ƃ��ă��b�v����ăR���|�[�l���g�Ɋ֘A�t�����܂��B</li>
 * </ul>
 * <p>{@link #RAW}�t�B�[���h�́C{@link PageLink#PageLink(String, Class)}��{@link ListView#ListView(String, java.util.List)}�Ȃǂ̂悤�ɁC
 * {@link IModel}�ȊO�̈��������R���X�g���N�^���g���ăR���|�[�l���g�𐶐��������ꍇ�Ɏg�p���܂��B{@link #RAW}�ȊO�̃t�B�[���h�́C
 * {@link Component#Component(String, IModel)}�̂悤�ɁC{@link IModel}�������Ɏ��R���X�g���N�^���g���ăR���|�[�l���g����������܂��B </p>
 * <p>{@link ModelType}��{@link WicketModel#type()}�����Ŏg�p�������ȉ��Ɏ����܂��B</p>
 * <pre>
 * &#064;WicketModel(type=ModelType.RAW)
 * private Class nextPage;
 * &#064;WicketComponent(modelName="nextPage")
 * private PageLink nextPageLink;
 * </pre>
 * <p>��L�ł�{@link #RAW}���w�肷�邱�Ƃɂ��CnextPageLink�I�u�W�F�N�g�̐������ɂ́C{@link PageLink#PageLink(String, Class)}�R���X�g���N�^��
 * �K�p����C��Q�����ɂ�nextPage�I�u�W�F�N�g�����̂܂ܓn����܂��B</p>
 * <pre>
 * &#064;WicketModel(type=ModelType.BASIC)
 * private String message;
 * &#064;WicketComponent(modelName="message")
 * private Label messageLabel;
 * </pre>
 * <p>��L�ł�{@link #BASIC}���w�肷�邱�Ƃɂ��Cmessage�I�u�W�F�N�g��{@link Model#Model(java.io.Serializable)}�R���X�g���N�^�ɓn�����
 * {@link Model}�I�u�W�F�N�g����������C{@link Label#Label(String, IModel)}�R���X�g���N�^�̑�Q�����ɓn�����messageLabel
 * �I�u�W�F�N�g����������܂��B</p>
 * <pre>
 * &#064;WicketModel(type=ModelType.PROPERTY)
 * private SearchCondition condition;
 * &#064;WicketComponent(modelName="condition", modelProperty="keyword")
 * private TextField keywordField;
 * </pre>
 * <p>��L�ł�{@link #PROPERTY}���w�肷�邱�Ƃɂ��Ccondition�I�u�W�F�N�g��{@link PropertyModel#PropertyModel(Object, String)}�R���X�g���N�^��
 * �n�����{@link PropertyModel}�I�u�W�F�N�g����������C{@link TextField#TextField(String, IModel)}�R���X�g���N�^�̑�Q�����ɓn�����
 * keywordField�I�u�W�F�N�g����������܂��B����́C</p>
 * <pre>
 * PropertyModel pm = new PropertyModel(condition, "keyword");
 * keywordField = new TextField("keywordField", pm);
 * add(keywordField);
 * </pre>
 * <p>�Ƃ����R�[�h�Ɠ����ł��B</p>
 * <pre>
 * &#064;WicketModel(type=ModelType.COMPOUND_PROPERTY)
 * private SearchCondition condition;
 * &#064;WicketComponent(modelName="condition")
 * private Form form;
 * </pre>
 * <p>��L�ł�{@link #COMPOUND_PROPERTY}���w�肷�邱�Ƃɂ��Ccondition�I�u�W�F�N�g��
 * {@link CompoundPropertyModel#CompoundPropertyModel(Object)}�R���X�g���N�^��
 * �n�����{@link CompoundPropertyModel}�I�u�W�F�N�g����������C{@link Form#Form(String, IModel)}�R���X�g���N�^�̑�Q�����ɓn�����
 * form�I�u�W�F�N�g����������܂��B����́C</p>
 * <pre>
 * CompoundPropertyModel cpm = new CompoundPropertyModel(condition);
 * form = new Form("form", cpm);
 * add(form);
 * </pre>
 * <p>�Ƃ����R�[�h�Ɠ����ł��B</p>
 * <pre>
 * &#064;WicketModel(type=ModelType.BOUND_COMPOUND_PROPERTY)
 * private SearchCondition condition;
 * &#064;WicketComponent(modelName="condition")
 * private Form form;
 * </pre>
 * <p>��L�ł�{@link #BOUND_COMPOUND_PROPERTY}���w�肷�邱�Ƃɂ��Ccondition�I�u�W�F�N�g��
 * {@link BoundCompoundPropertyModel#BoundCompoundPropertyModel(Object)}�R���X�g���N�^��
 * �n�����{@link BoundCompoundPropertyModel}�I�u�W�F�N�g����������C{@link Form#Form(String, IModel)}�R���X�g���N�^�̑�Q�����ɓn�����
 * form�I�u�W�F�N�g����������܂��B����́C</p>
 * <pre>
 * BoundCompoundPropertyModel bcpm = new BoundCompoundPropertyModel(condition);
 * form = new Form("form", bcpm);
 * add(form);
 * </pre>
 * <p>�Ƃ����R�[�h�Ɠ����ł��B{@link #BOUND_COMPOUND_PROPERTY}�t�B�[���h���g�p�����ꍇ�C���̃��f�����֘A�t����ꂽ�R���|�[�l���g��e�Ɏ���
 * �R���|�[�l���g����`���ꂽ���ɁC���̃R���|�[�l���g�ƃ��f���������I��{@link BoundCompoundPropertyModel#bind(Component, String)}
 * ���\�b�h�����s���ăo�C���h����܂��B�Ⴆ�΁C</p>
 * <pre>
 * &#064;WicketComponent(parent="form", modelProperty="keyword")
 * private TextField keywordField;
 * </pre>
 * <p>�Ƃ�����`�́C</p>
 * <pre>
 * keywordField = new TextField("keywordField");
 * bcpm.bind(keywordField, "keyword");
 * form.add(keywordField);
 * </pre>
 * <p>�Ƃ����R�[�h�Ɠ����ł��B</p>
 * 
 * @see WicketModel
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
public enum ModelType {

	/**
	 * ���̃��f���I�u�W�F�N�g�����̂܂܃R���|�[�l���g�Ɋ֘A�t���܂��B
	 */
	RAW,
	
	/**
	 * ��{���f���Ƃ��āC���̃��f���I�u�W�F�N�g���R���|�[�l���g�Ɋ֘A�t�����܂��B
	 */
	BASIC,
	
	/**
	 * �v���p�e�B���f���Ƃ��āC���̃��f���I�u�W�F�N�g���R���|�[�l���g�Ɋ֘A�t�����܂��B
	 */
	PROPERTY,
	
	/**
	 * �����v���p�e�B���f���Ƃ��āC���̃��f���I�u�W�F�N�g���R���|�[�l���g�Ɋ֘A�t�����܂��B
	 */
	COMPOUND_PROPERTY,
	
	/**
	 * �����o�C���h�v���p�e�B���f���Ƃ��āC���̃��f���I�u�W�F�N�g���R���|�[�l���g�Ɋ֘A�t�����܂��B
	 */
	BOUND_COMPOUND_PROPERTY
	
}
