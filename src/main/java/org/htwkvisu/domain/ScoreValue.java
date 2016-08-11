package org.htwkvisu.domain;

import com.vividsolutions.jts.geom.Point;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigInteger;

import static org.htwkvisu.domain.NamedQueryConstants.*;

@NamedQueries({
        @NamedQuery(name = FIND_TERMINAL, query = FROM_WHERE + " s.aeroway='terminal'"),
        @NamedQuery(name = FIND_HELIPAD, query = FROM_WHERE + " s.aeroway='helipad'"),
        @NamedQuery(name = FIND_AERODROME, query = FROM_WHERE + " s.aeroway='aerodrome'"),
        @NamedQuery(name = FIND_BUS, query = FROM_WHERE + " s.bus='yes'"),
        @NamedQuery(name = FIND_TRAIN, query = FROM_WHERE + " s.train='yes'"),
        @NamedQuery(name = FIND_TRAM, query = FROM_WHERE + " s.tram='yes'"),
        @NamedQuery(name = FIND_HOSPITAL, query = FROM_WHERE + " s.amenity='hospital'"),
        @NamedQuery(name = FIND_PHARMACY, query = FROM_WHERE + " s.amenity='pharmacy'"),
        @NamedQuery(name = FIND_DENTIST, query = FROM_WHERE + " s.amenity='dentist'"),
        @NamedQuery(name = FIND_VETERINARY, query = FROM_WHERE + " s.amenity='veterinary'"),
        @NamedQuery(name = FIND_DOCTORS, query = FROM_WHERE + " s.amenity='doctors'"),
        @NamedQuery(name = FIND_BLOOD_DONATION, query = FROM_WHERE + " s.healthcare='blood_donation'"),
        @NamedQuery(name = FIND_SCHOOL, query = FROM_WHERE + " s.amenity='school'"),
        @NamedQuery(name = FIND_COLLEGE, query = FROM_WHERE + " s.amenity='college'"),
        @NamedQuery(name = FIND_LIBRARY, query = FROM_WHERE + " s.amenity='library'"),
        @NamedQuery(name = FIND_THEATRE, query = FROM_WHERE + " s.amenity='theatre'"),
        @NamedQuery(name = FIND_MUSEUM, query = FROM_WHERE + " s.tourism='museum'"),
        @NamedQuery(name = FIND_RESEARCH_INSTITUTION, query = FROM_WHERE + " s.researchInstitution='yes'")
})
@Entity(name = "score_value")
public class ScoreValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poi_id", nullable = false)
    private BigInteger poiId;

    @Column(name = "osm_id", nullable = false)
    private BigInteger osmId;

    @Column(name = "point", columnDefinition = "Geometry", nullable = false)
    @Type(type = "org.htwkvisu.domain.GeometryType")
    private Point point;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "aeroway")
    private String aeroway;

    @Column(name = "bus")
    private String bus;

    @Column(name = "tram")
    private String tram;

    @Column(name = "train")
    private String train;

    @Column(name = "railway")
    private String railway;

    @Column(name = "amenity")
    private String amenity;

    @Column(name = "healthcare")
    private String healthcare;

    @Column(name = "tourism")
    private String tourism;

    @Column(name = "research_institution")
    private String researchInstitution;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAeroway() {
        return this.aeroway;
    }

    public void setAeroway(String aeroway) {
        this.aeroway = aeroway;
    }

    public String getBus() {
        return this.bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getTram() {
        return this.tram;
    }

    public void setTram(String tram) {
        this.tram = tram;
    }

    public String getTrain() {
        return this.train;
    }

    public void setTrain(String train) {
        this.train = train;
    }

    public String getRailway() {
        return this.railway;
    }

    public void setRailway(String railway) {
        this.railway = railway;
    }

    public String getAmenity() {
        return this.amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public String getHealthcare() {
        return this.healthcare;
    }

    public void setHealthcare(String healthcare) {
        this.healthcare = healthcare;
    }

    public String getTourism() {
        return this.tourism;
    }

    public void setTourism(String tourism) {
        this.tourism = tourism;
    }

    public String getResearchInstitution() {
        return this.researchInstitution;
    }

    public void setResearchInstitution(String researchInstitution) {
        this.researchInstitution = researchInstitution;
    }

    public BigInteger getPoiId() {
        return this.poiId;
    }

    public void setPoiId(BigInteger poiId) {
        this.poiId = poiId;
    }

    public BigInteger getOsmId() {
        return this.osmId;
    }

    public void setOsmId(BigInteger osmId) {
        this.osmId = osmId;
    }

    public Point getPoint() {
        return this.point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "ScoreValue{" +
                "poiId=" + poiId +
                ", osmId=" + osmId +
                ", point=" + point +
                ", name='" + name + '\'' +
                ", aeroway='" + aeroway + '\'' +
                ", bus='" + bus + '\'' +
                ", tram='" + tram + '\'' +
                ", train='" + train + '\'' +
                ", railway='" + railway + '\'' +
                ", amenity='" + amenity + '\'' +
                ", healthcare='" + healthcare + '\'' +
                ", tourism='" + tourism + '\'' +
                ", researchInstitution='" + researchInstitution + '\'' +
                '}';
    }
}
