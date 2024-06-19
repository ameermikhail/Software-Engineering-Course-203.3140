package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "docx_files")
public class FileEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "docx_file_data", columnDefinition = "MEDIUMBLOB")
    private byte[] docxFileData;

    // Getters and Setters (omitted for brevity)
}
