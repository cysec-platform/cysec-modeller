/*-
 * #%L
 * CYSEC Modeller App
 * %%
 * Copyright (C) 2021 - 2022 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.smesec.cysec.modeller.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matthias Luppi
 */
@Entity
public class Control {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String key;

    @Column(length = 511)
    private String name;

    @Column(length = 2000)
    private String details;

    @ManyToOne
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "CONTROL_DEPENDENCIES",
            joinColumns = { @JoinColumn(name = "CONTROL_ID") },
            inverseJoinColumns = { @JoinColumn(name = "DEPENDENCY_CONTROL_ID") }
    )
    private final List<Control> dependencies = new ArrayList<>();

    @ManyToMany(mappedBy = "dependencies")
    private final List<Control> dependents = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "CONTROL_SOURCES",
            joinColumns = { @JoinColumn(name = "CONTROL_ID") },
            inverseJoinColumns = { @JoinColumn(name = "EXTERNAL_CONTROL_ID") }
    )
    private final List<ExternalControl> sources = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public List<Control> getDependencies() {
        return dependencies;
    }

    public List<Control> getDependents() {
        return dependents;
    }

    public List<ExternalControl> getSources() {
        return sources;
    }
}
