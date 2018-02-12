package ugh.dl;

/*******************************************************************************
 * ugh.dl / FileSet.java
 *
 * Copyright 2010 Center for Retrospective Digitization, Göttingen (GDZ)
 *
 * http://gdz.sub.uni-goettingen.de
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version.
 *
 * This Library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.kitodo.api.ugh.ContentFileInterface;
import org.kitodo.api.ugh.FileSetInterface;
import org.kitodo.api.ugh.VirtualFileGroupInterface;

/*******************************************************************************
 * <p>
 * A <code>FileSet</code> contains all ContentFiles which belong to a
 * DigitalDocument. The class provides methods to add or remove ContentFile
 * objects. Each ContentFile object can only be added once. The
 * <code>FileSet</code> also.
 * </p>
 *
 * <p>
 * Beside grouping ContentFiles a FileSet can store Metadata. This Metadata is
 * valid for the all ContentFiles. In opposite to the DocStruct objects, there
 * is no validation when adding Metadata objects to a FileSet. A FileSet can
 * contain any and as many metadata as possible.
 * </p>
 *
 * @author Markus Enders
 * @version 2010-01-22
 * @since 2008-12-05
 * @see ContentFile
 *
 ******************************************************************************/

public class FileSet implements FileSetInterface, Serializable {

    private static final long        serialVersionUID    = 6605222755528016675L;

    private static final String        LINE                = "--------------------"
                                                                + "--------------------"
                                                                + "--------------------"
                                                                + "--------------------";

    // Containing all Images/Files belonging to a digital object.
    private List<ContentFileInterface>        allImages;

    // Metadata for the image/fileset.
    private List<Metadata>            allMetadata;

    // If metadata is removed; it will be added to this list; for undo
    // functions, or something else lateron.
    private List<Metadata>            removedMetadata;

    // Contains all virtual fileg groups needed for the zvdd/DFG-viewer METS.
    private List<VirtualFileGroup>    virtualFileGroups;

    /***************************************************************************
     * <p>
     * Constructor. Creates all lists which store all objects for Images,
     * Metadata, removed Metadata, and virtual file groups.
     * </p>
     **************************************************************************/
    public FileSet() {
        this.allImages = new LinkedList<ContentFileInterface>();
        this.allMetadata = new LinkedList<Metadata>();
        this.removedMetadata = new LinkedList<Metadata>();
        this.virtualFileGroups = new LinkedList<VirtualFileGroup>();
    }

    /***************************************************************************
     * <p>
     * Adds a ContentFile object to the FileSet, if it is not yet existing.
     * </p>
     *
     * @param inImage
     *            ContentFile to be added
     * @return always true
     **************************************************************************/
    @Override
    public void addFile(ContentFileInterface inImage) {

        // Only add the file, if it is not yet existing in the list.
        if (!this.allImages.contains(inImage)) {
            this.allImages.add(inImage);
        }
    }

    /***************************************************************************
     * <p>
     * Removes a ContentFile from the FileSet. If the ContentFile doesn't belong
     * to the FileSet an exception is thrown.
     * </p>
     *
     * @param inImage
     *            ContentFile to be removed
     **************************************************************************/
    @Override
    public void removeFile(ContentFileInterface inImage) {
        this.allImages.remove(inImage);
    }

    /***************************************************************************
     * @param inMD
     * @return
     **************************************************************************/
    public boolean addMetadata(Metadata inMD) {
        this.allMetadata.add(inMD);
        return true;
    }

    /***************************************************************************
     * @param inMD
     * @return
     **************************************************************************/
    public boolean removeMetadata(Metadata inMD) {
        this.removedMetadata.add(inMD);
        this.allMetadata.remove(inMD);
        return true;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    @Override
    public Collection<ContentFileInterface> getAllFiles() {
        return this.allImages;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<Metadata> getAllMetadata() {
        return this.allMetadata;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<VirtualFileGroup> getVirtualFileGroups() {
        return this.virtualFileGroups;
    }

    /**************************************************************************
     * @param theVirtualFileGroupList
     **************************************************************************/
    public void setVirtualFileGroups(
            List<VirtualFileGroup> theVirtualFileGroupList) {
        this.virtualFileGroups = theVirtualFileGroupList;
    }

    /***************************************************************************
     * @param virtualFileGroups
     **************************************************************************/
    @Override
    public void addVirtualFileGroup(VirtualFileGroupInterface theFilegroup) {
        this.virtualFileGroups.add((VirtualFileGroup) theFilegroup);
    }

    /***************************************************************************
     * @param theFilegroup
     **************************************************************************/
    public void removeVirtualFileGroup(VirtualFileGroup theFilegroup) {
        this.virtualFileGroups.remove(theFilegroup);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        String result = LINE + "\nFileSet\n" + LINE + "\n";

        // Add FileSet.
        if (this.getAllFiles() == null || this.getAllFiles().isEmpty()) {
            result += "NO FILES" + "\n";
        } else {
            StringBuffer resultBuffer = new StringBuffer();
            for (ContentFileInterface currentCF : this.getAllFiles()) {
                resultBuffer.append("ContentFile (" + ((ContentFile) currentCF).getIdentifier()
                        + "): '" + currentCF.getLocation() + "' ("
                        + ((ContentFile) currentCF).getMimetype() + ")" + "\n");
            }
            result += resultBuffer;
        }

        // Add VirtualFileGroups.
        result += LINE + "\nVirtualFileGroups\n" + LINE + "\n";

        if (this.getVirtualFileGroups() == null
                || this.getVirtualFileGroups().isEmpty()) {
            result += "NONE\n";
        } else {
            StringBuffer resultBuffer = new StringBuffer();
            for (VirtualFileGroup vfg : this.getVirtualFileGroups()) {
                resultBuffer.append("NAME: " + vfg.getName() + ", FILESUFFIX: "
                        + vfg.getFileSuffix() + ", MIMETYPE: "
                        + vfg.getMimetype() + ", IDSUFFIX: "
                        + vfg.getIdSuffix() + ", PATH: " + vfg.getPathToFiles()
                        + "\n");
            }
            result += resultBuffer;
        }

        return result;
    }

}
