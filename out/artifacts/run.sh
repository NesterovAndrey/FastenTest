#!/bin/bash
argStr=""
addParam()
{
 argStr=${argStr}"${1} ${2} "
}

while [[ $# -gt 1 ]]
do
key="$1"

case $key in
    -d)
    addParam "-d" "${2}" 
    shift # past argument
    ;;
    -db)
    addParam "-db" "${2}" 
    shift # past argument
    ;;
    -u)
    addParam "-u" "${2}" 
    shift # past argument
    ;;
    -p)
    addParam "-p" "${2}" 
    shift # past argument
    ;;
    -req)
    addParam "-req" "${2}" 
    shift # past argument
    ;;
    -date)
    addParam "-date" "$2" 
    shift # past argument
    ;;
    -day)
    addParam "-day" "$2" 
    shift # past argument
    ;;
    -month)
    addParam "-month" "${2}" 
    shift # past argument
    ;;
    -year)
    addParam "-year" "${2}" 
    shift # past argument
    ;;
    -rf)
    addParam "-rf" "${2}" 
    addParam "-if" "${2}"
    shift # past argument
    ;;
    -rfl)
    addParam "-rfl" "{$2}"
    addParam "-ifl" "{$2}"  
    shift # past argument
    ;;
    -ft)
    addParam "-ft" "{$2}" 
    shift # past argument
    ;;
    -f)
    addParam "-f" "$2" 
    shift # past argument
    ;;
    -t)
    addParam "-t" "{$2}" 
    shift # past argument
    ;;
    *)
            # unknown option
    ;;
esac
shift # past argument or value
done
dataStr=./lib/*:./connector/*
echo "Started with args:"
echo ${argStr}
gnome-terminal -x bash -c "
echo Unloading started;
java -cp 'unloading.jar:$dataStr' UnloadingMain $argStr;
echo Unloading completed;
echo Processing start;
java -cp 'processing.jar:$dataStr' ProcessingMain $argStr;
echo Processing completed;
echo Uploading start;
java -cp 'uploading.jar:$dataStr' DataUploaderMain $argStr;
echo Uploading complete;
echo End;
bash"

