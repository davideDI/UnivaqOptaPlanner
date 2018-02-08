package it.univaq.planner.business.model;

import java.io.Serializable;

public class Resource implements Serializable {

	private static final long serialVersionUID = -9155930370931341722L;
	
	public Resource() {
		super();
	}
	
	public Resource(Long id, String name, String description, Integer capacity, Boolean projector, Boolean screenMotor,
			Boolean screenManual, Boolean audio, Boolean pc, Boolean wireMic, Boolean wirelessMic,
			Boolean overheadProjector, Boolean visualPresenter, Boolean wiring, Boolean equipment, String blackboard,
			String note, Integer network, Short idTipResource, Group group) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.capacity = capacity;
		this.projector = projector;
		this.screenMotor = screenMotor;
		this.screenManual = screenManual;
		this.audio = audio;
		this.pc = pc;
		this.wireMic = wireMic;
		this.wirelessMic = wirelessMic;
		this.overheadProjector = overheadProjector;
		this.visualPresenter = visualPresenter;
		this.wiring = wiring;
		this.equipment = equipment;
		this.blackboard = blackboard;
		this.note = note;
		this.network = network;
		this.idTipResource = idTipResource;
		this.group = group;
	}

	private Long id;
	private String name;
	private String description;
	private Integer capacity;
	private Boolean projector;
	private Boolean screenMotor;
	private Boolean screenManual;
	private Boolean audio;
	private Boolean pc;
	private Boolean wireMic;
	private Boolean wirelessMic;
	private Boolean overheadProjector;
	private Boolean visualPresenter;
	private Boolean wiring;
	private Boolean equipment;
	private String blackboard;
	private String note;
	private Integer network;
	
	private Short idTipResource;
	private Group group;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	public Boolean getProjector() {
		return projector;
	}
	public void setProjector(Boolean projector) {
		this.projector = projector;
	}
	public Boolean getScreenMotor() {
		return screenMotor;
	}
	public void setScreenMotor(Boolean screenMotor) {
		this.screenMotor = screenMotor;
	}
	public Boolean getScreenManual() {
		return screenManual;
	}
	public void setScreenManual(Boolean screenManual) {
		this.screenManual = screenManual;
	}
	public Boolean getAudio() {
		return audio;
	}
	public void setAudio(Boolean audio) {
		this.audio = audio;
	}
	public Boolean getPc() {
		return pc;
	}
	public void setPc(Boolean pc) {
		this.pc = pc;
	}
	public Boolean getWireMic() {
		return wireMic;
	}
	public void setWireMic(Boolean wireMic) {
		this.wireMic = wireMic;
	}
	public Boolean getWirelessMic() {
		return wirelessMic;
	}
	public void setWirelessMic(Boolean wirelessMic) {
		this.wirelessMic = wirelessMic;
	}
	public Boolean getOverheadProjector() {
		return overheadProjector;
	}
	public void setOverheadProjector(Boolean overheadProjector) {
		this.overheadProjector = overheadProjector;
	}
	public Boolean getVisualPresenter() {
		return visualPresenter;
	}
	public void setVisualPresenter(Boolean visualPresenter) {
		this.visualPresenter = visualPresenter;
	}
	public Boolean getWiring() {
		return wiring;
	}
	public void setWiring(Boolean wiring) {
		this.wiring = wiring;
	}
	public Boolean getEquipment() {
		return equipment;
	}
	public void setEquipment(Boolean equipment) {
		this.equipment = equipment;
	}
	public String getBlackboard() {
		return blackboard;
	}
	public void setBlackboard(String blackboard) {
		this.blackboard = blackboard;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Integer getNetwork() {
		return network;
	}
	public void setNetwork(Integer network) {
		this.network = network;
	}
	public Short getIdTipResource() {
		return idTipResource;
	}
	public void setIdTipResource(Short idTipResource) {
		this.idTipResource = idTipResource;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Resource [id=" + id + ", name=" + name + ", description=" + description + ", capacity=" + capacity
				+ ", projector=" + projector + ", screenMotor=" + screenMotor + ", screenManual=" + screenManual
				+ ", audio=" + audio + ", pc=" + pc + ", wireMic=" + wireMic + ", wirelessMic=" + wirelessMic
				+ ", overheadProjector=" + overheadProjector + ", visualPresenter=" + visualPresenter + ", wiring="
				+ wiring + ", equipment=" + equipment + ", blackboard=" + blackboard + ", note=" + note + ", network="
				+ network + ", idTipResource=" + idTipResource + ", group=" + group + "]";
	}
	
}
