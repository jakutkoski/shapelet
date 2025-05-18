package com.shapelet.utility

class GetOffsetException(id: Int): Exception("cannot get offset of node $id since it does not exist")

class BoardTypeException(type: String): Exception("board type $type does not exist")

class InvalidIdException(id: Int, boardType: String): Exception("id $id is not valid for board type $boardType")