#!/bin/bash

RED='\e[1m\e[31m'
GREEN='\e[1m\e[92m'
NORMAL='\e[0m'
set -e

while [[ $# -gt 0 ]]
do
key="$1"

case ${key} in
    -f|--file)
    FILE="$2"
    shift
    ;;
    -c|--clear-database)
    CLEAR_DB=true
    ;;
    -h|--help)
    echo -e "-f --file path/to/file\t\tFile which contains OSM Nodes\n-c --clear-database \t\tIf set, records from table score_value will be deleted before inserting new records."
    exit 0
    ;;
    *)
    echo "Unknown parameter $1."
    exit 1
    ;;
esac
shift
done

CSV_FILE="tmp/filtered.csv"

if ! [ -f "$FILE" ]; then
    echo -e "${RED}Please provide a osm file, e.g. ./import.sh -f nodes.osm"
    exit 1
fi

if [ -d "tmp/" ]; then
	echo -n -e "${NORMAL}Cleaning old tmp files...\t\t"
	rm -rf tmp/
	if [ $? -eq 0 ]; then
	    echo -e ${GREEN}OK
	else
	    echo -e ${RED}FAIL
	fi
fi

echo -n -e "${NORMAL}Creating tmp directory...\t\t"
mkdir tmp
if [ $? -eq 0 ]; then
    echo -e ${GREEN}OK
else
    echo -e ${RED}FAIL
fi

echo -n -e "${NORMAL}Convert OSM nodes to CSV...\t\t"
bin/osmconvert64 ${FILE} --csv="@id @lat @lon name aeroway bus tram train railway amenity healthcare tourism research_institution" -o=${CSV_FILE}  --csv-separator=,
if [ $? -eq 0 ]; then
    echo -e ${GREEN}OK
else
    echo -e ${RED}FAIL
fi

echo -n -e "${NORMAL}Create Database life_quality...\t\t"
sudo mysql -e "CREATE DATABASE IF NOT EXISTS life_quality;"
if [ $? -eq 0 ]; then
    echo -e ${GREEN}OK
else
    echo -e ${RED}FAIL
fi

FILTERED_CNT=$(awk '{n+=1} END {print n}' ${CSV_FILE})

if [ "${CLEAR_DB}" = true ] ; then
    echo -n -e "${NORMAL}Drop table life_quality...\t\t"
    sudo mysql -e "DROP TABLE IF EXISTS score_value" life_quality
    if [ $? -eq 0 ]; then
        echo -e ${GREEN}OK
    else
        echo -e ${RED}FAIL
    fi
fi

echo -n -e "${NORMAL}Creating table...\t\t\t"
sudo mysql -e "CREATE TABLE IF NOT EXISTS score_value ( poi_id BIGINT unsigned NOT NULL auto_increment, PRIMARY KEY  (poi_id) , osm_id BIGINT,  point POINT NOT NULL, SPATIAL INDEX(point) , name TEXT , aeroway TEXT , bus TEXT , tram TEXT , train TEXT , railway TEXT , amenity TEXT , healthcare TEXT , tourism TEXT , research_institution TEXT ) ENGINE=MyISAM;" life_quality
if [ $? -eq 0 ]; then
    echo -e ${GREEN}OK
else
    echo -e ${RED}FAIL
fi

echo -n -e "${NORMAL}Creating trigger...\t\t\t"
sudo mysql life_quality -e "delimiter //
CREATE TRIGGER score_value_name_not_empty BEFORE INSERT ON score_value FOR EACH ROW
BEGIN
        IF NEW.name = '' THEN
                SIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = 'Blank value on score_value.name';
        END IF;
END;//
delimiter ;
"
if [ $? -eq 0 ]; then
    echo -e ${GREEN}OK
else
    echo -e ${RED}FAIL
fi


cp ${CSV_FILE} tmp/inserts.sql
echo -n -e "${NORMAL}Creating SQL statements...\t\t"
# replace empty CSV value with NULL
sed 's/^,/NULL,/; :a;s/,,/,NULL,/g;ta' -i tmp/inserts.sql
# replace " with '
sed -e ':a' -e 'N' -e '$!ba' -e 's/\"/\x27/g' -i tmp/inserts.sql
# enquote every value
sed 's/[^,][^,]*/"&"/g' -i tmp/inserts.sql
# replace ,, with ,NULL,
sed 's/,,/,NULL,NULL,/g' -i tmp/inserts.sql
# replace ,, with ,
sed 's/,,/,/g' -i tmp/inserts.sql
# add INSERT IGNORE INTO score_value VALUES (NULL, before each line
sed 's/^/INSERT IGNORE INTO score_value VALUES (NULL,/' -i tmp/inserts.sql
# add ); at the end of each line
sed 's/$/);/' -i tmp/inserts.sql
# replace ,); with ,""); - if research_institute is NULL
sed 's/,);/,"");/g' -i tmp/inserts.sql
# replace "NULL" with ""
sed 's/"NULL"/""/g' -i tmp/inserts.sql

# Create Point(lat lon) from "lat", "lon"
sed 's/,"/,ST_PointFromText("POINT(/2' -i tmp/inserts.sql
sed 's/,/ /3' -i tmp/inserts.sql
sed 's/",/)"),/2' -i tmp/inserts.sql
sed 's/"//4' -i tmp/inserts.sql
sed 's/"//4' -i tmp/inserts.sql
if [ $? -eq 0 ]; then
    echo -e ${GREEN}OK
else
    echo -e ${RED}FAIL
fi

echo -n -e "${NORMAL}Executing SQL statements...\t\t"
sudo mysql life_quality -f < tmp/inserts.sql
if [ $? -eq 0 ]; then
    echo -e ${GREEN}OK
else
    echo -e ${RED}FAIL
fi

ROW_CNT=$(sudo mysql life_quality --raw --batch -e 'select count(*) from score_value' -s)
echo -e "${NORMAL}Imported records: \t\t\t$ROW_CNT"

if ! [ ${ROW_CNT} == ${FILTERED_CNT} ]; then
    echo -e "${RED}WARNING: Imported $FILTERED_CNT records, but MySQL inserted $ROW_CNT records!";
fi

echo -n -e "${NORMAL}Cleaning tmp files...\t\t\t"
rm -r tmp/
if [ $? -eq 0 ]; then
    echo -e ${GREEN}OK
else
    echo -e ${RED}FAIL
fi
echo -e "${GREEN}Finished successfully."
exit 0
