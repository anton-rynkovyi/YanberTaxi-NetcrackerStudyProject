begin
  execute immediate 'drop table OBJTYPE cascade constraint';
  exception
  when others then null;
end;
/
CREATE TABLE OBJTYPE
(
  OBJECT_TYPE_ID NUMBER(20) NOT NULL ENABLE,
  PARENT_ID      NUMBER(20),
  CODE           VARCHAR2(20) ,
  NAME           VARCHAR2(200 BYTE),
  DESCRIPTION    VARCHAR2(1000 BYTE),
  CONSTRAINT CON_OBJECT_TYPE_ID PRIMARY KEY (OBJECT_TYPE_ID),
  CONSTRAINT CON_PARENT_ID FOREIGN KEY (PARENT_ID) REFERENCES OBJTYPE (OBJECT_TYPE_ID) ON DELETE CASCADE deferrable
);

begin
  execute immediate 'drop table ATTRTYPE cascade constraint';
  exception
  when others then null;
end;
/
  CREATE TABLE ATTRTYPE (
  ATTR_ID      		NUMBER(20) NOT NULL,
  OBJECT_TYPE_ID 		NUMBER(20) NOT NULL,
  OBJECT_TYPE_ID_REF 	NUMBER(20),
  CODE         		VARCHAR2(20),
  NAME         		VARCHAR2(200 BYTE),
  CONSTRAINT CON_ATTR_ID PRIMARY KEY (ATTR_ID),
  CONSTRAINT CON_ATTR_OBJECT_TYPE_ID FOREIGN KEY (OBJECT_TYPE_ID) REFERENCES OBJTYPE (OBJECT_TYPE_ID) deferrable,
  CONSTRAINT CON_ATTR_OBJECT_TYPE_ID_REF FOREIGN KEY (OBJECT_TYPE_ID_REF) REFERENCES OBJTYPE (OBJECT_TYPE_ID) deferrable
);

begin
  execute immediate 'drop table OBJECTS cascade constraint';
  exception
  when others then null;
end;
/

CREATE TABLE OBJECTS (
  OBJECT_ID      NUMBER(20) NOT NULL,
  PARENT_ID      NUMBER(20),
  OBJECT_TYPE_ID NUMBER(20) NOT NULL,
  NAME           VARCHAR2(2000 BYTE),
  DESCRIPTION    VARCHAR2(4000 BYTE),
  CONSTRAINT CON_OBJECTS_ID PRIMARY KEY (OBJECT_ID),
  CONSTRAINT CON_PARENTS_ID FOREIGN KEY (PARENT_ID) REFERENCES OBJECTS (OBJECT_ID) ON DELETE CASCADE,
  CONSTRAINT CON_OBJ_TYPE_ID FOREIGN KEY (OBJECT_TYPE_ID) REFERENCES OBJTYPE (OBJECT_TYPE_ID)
);
begin
  execute immediate 'drop table LISTS cascade constraint';
  exception
  when others then null;
end;
/
create table LISTS
(
  attr_id number(20) not null,
  list_value_id number(20) not null,
  value varchar(4000),
  constraint con_lattr_id foreign key (attr_id) references attrtype (attr_id) on delete cascade
);
begin
  execute immediate 'drop table ATTRIBUTES cascade constraint';
  exception
  when others then null;
end;
/

CREATE TABLE ATTRIBUTES
(
  ATTR_ID    NUMBER(20) NOT NULL,
  OBJECT_ID  NUMBER(20) NOT NULL,
  VALUE      VARCHAR2(4000 BYTE),
  DATE_VALUE DATE,
  list_value_id number(20),
  CONSTRAINT CON_AOBJECT_ID FOREIGN KEY (OBJECT_ID) REFERENCES OBJECTS (OBJECT_ID) ON DELETE CASCADE,
  CONSTRAINT CON_AATTR_ID FOREIGN KEY (ATTR_ID) REFERENCES ATTRTYPE (ATTR_ID) ON DELETE CASCADE
);

begin
  execute immediate 'drop table OBJREFERENCE cascade constraint';
  exception
  when others then null;
end;
/
  CREATE TABLE OBJREFERENCE
(
  ATTR_ID   NUMBER(20) NOT NULL,
  REFERENCE NUMBER(20) NOT NULL,
  OBJECT_ID NUMBER(20) NOT NULL,
  CONSTRAINT CON_OBJREFERENCE_PK PRIMARY KEY (ATTR_ID,REFERENCE,OBJECT_ID),
  CONSTRAINT CON_REFERENCE FOREIGN KEY (REFERENCE) REFERENCES OBJECTS (OBJECT_ID) ON DELETE CASCADE,
  CONSTRAINT CON_ROBJECT_ID FOREIGN KEY (OBJECT_ID) REFERENCES OBJECTS (OBJECT_ID) ON DELETE CASCADE,
  CONSTRAINT CON_RATTR_ID FOREIGN KEY (ATTR_ID) REFERENCES ATTRTYPE (ATTR_ID) ON DELETE CASCADE
);

begin
  execute immediate 'drop table Users cascade constraint';
  exception
  when others then null;
end;
/
CREATE TABLE Users
(
  user_id     NUMBER(20)  PRIMARY KEY,
  object_id   NUMBER(20) REFERENCES OBJECTS (object_id),
  login       VARCHAR2(50) NOT NULL UNIQUE,
  password    VARCHAR2(200) NOT NULL,
  role        VARCHAR2(50)
) ;


COMMIT;